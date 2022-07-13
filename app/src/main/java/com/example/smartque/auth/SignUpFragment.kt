package com.example.smartque.auth

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.smartque.R
import com.example.smartque.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding?=null
    private val binding get()=_binding!!
    private lateinit var auth: FirebaseAuth
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentSignUpBinding.inflate(inflater,container,false)
        initViews()
        auth = Firebase.auth
        return binding.root
    }

    private fun initViews() {
        binding.toLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        binding.signup.setOnClickListener {
            confirmDetails()
        }
    }

    private fun confirmDetails() {
        val name = binding.profileName.editText?.toString()
        val email = binding.email.editText?.toString()
        val password = binding.password.editText?.toString()
        val confirmPassword= binding.confirmPassword.editText.toString()
        if(name.isNullOrBlank() || email.isNullOrBlank() || email.isNullOrBlank() ||
            password.isNullOrBlank() || confirmPassword.isNullOrBlank()){
            Toast.makeText(requireContext(),"All fields must be filled",Toast.LENGTH_LONG).show()
        }else{
            if (passwordMatch(password, confirmPassword) && validateEmail(email)){
                //save user, in the db then sign in.
                signIn(email, password)
            }
        }

    }

    private fun signIn(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if(task.isSuccessful){
                    val user = auth.currentUser
                    this.findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToHomeFragment())
                }
            }
            .addOnFailureListener{e->
                Toast.makeText(requireContext(),"${e.localizedMessage}",Toast.LENGTH_LONG).show()
            }
    }

    private fun passwordMatch(pswd: String, cpswd: String):Boolean{
        return pswd.equals(cpswd)
    }
    private fun validateEmail(email:String): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}