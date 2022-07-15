package com.example.smartque.auth

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.smartque.databinding.FragmentLoginBinding

import com.example.smartque.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        val email = binding.email.editText?.toString()
        val password = binding.password.editText?.toString()
        if(email.isNullOrBlank() || email.isNullOrBlank() ||
            password.isNullOrBlank()){
            Toast.makeText(requireContext(),"All fields must be filled",Toast.LENGTH_LONG).show()
        }else{
            if (validateEmail(email)){
                //save user, in the db then sign in.
                signIn(email, password)
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
                   val user = auth.currentUser
                   //navigate home.
                   this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
               }
           }
           .addOnFailureListener{e->
               Toast.makeText(requireContext(),"${e.localizedMessage}", Toast.LENGTH_LONG).show()
           }
    }

    private fun validateEmail(email:String): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}