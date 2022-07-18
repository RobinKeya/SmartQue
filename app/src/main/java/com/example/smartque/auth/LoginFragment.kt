package com.example.smartque.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartque.R
import com.example.smartque.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private var authState = FirebaseAuth.AuthStateListener{firebaseAuth->
        val user =firebaseAuth.currentUser
        if(user!=null){
            if (user.isEmailVerified){
                this.findNavController().navigate(LoginFragmentDirections
                    .actionLoginFragmentToHomeFragment())
            }else{
                Toast.makeText(requireContext(),"Please verify your email",Toast.LENGTH_SHORT)
                    .show()
                firebaseAuth.signOut()
            }
        }

    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(authState)
    }

    override fun onStop() {
        super.onStop()
        if (authState !=null){
            FirebaseAuth.getInstance().removeAuthStateListener(authState)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews()
        auth = Firebase.auth
        _binding = FragmentLoginBinding.inflate(inflater)
        return binding.root

    }

    private fun initViews() {
        binding.toSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        binding.loginBtn.setOnClickListener{
            confirmDetails()
        }
    }

    private fun confirmDetails() {
        showDialog()
        val email = binding.email.editText?.text.toString()
        val password = binding.password.editText?.text.toString()
        if(email.isBlank() || email.isBlank() ||
            password.isBlank()){
            Toast.makeText(requireContext(),"All fields must be filled",Toast.LENGTH_LONG)
                .show()
        }else{
            if (validateEmail(email)){
                //save user, in the db then sign in.
                signIn(email, password)
            }else{
                hideDialog()
                Toast.makeText(requireContext(), "Invalid email",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun signIn(email: String, password: String) {
       auth.signInWithEmailAndPassword(email,password)
           .addOnCompleteListener{task->
               if (task.isSuccessful){
                   //val user = auth.currentUser
                   hideDialog()
               }
           }
           .addOnFailureListener{e->
               Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_LONG).show()
           }
    }

    private fun validateEmail(email:String): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun showDialog(){
        binding.progressbar.visibility = View.VISIBLE
    }
    private fun hideDialog(){
        if(binding.progressbar.visibility ==View.VISIBLE){
            binding.progressbar.visibility = View.INVISIBLE
        }
    }
}