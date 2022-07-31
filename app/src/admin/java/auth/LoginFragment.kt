package com.example.smartq.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartq.R
import com.example.smartq.databinding.FragmentLoginBinding
import com.example.smartq.helper.Constant
import com.example.smartq.helper.PrefHelper
import com.example.smartq.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var prefHelper: PrefHelper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater)
        auth = Firebase.auth
        initViews()
        prefHelper = PrefHelper(requireContext())
        return binding.root

    }

    private fun initViews() {
        binding.toSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        binding.loginBtn.setOnClickListener{
            showDialog()
            confirmDetails()
        }
    }

    private fun confirmDetails() {
        val email = binding.email.editText?.text.toString()
        val password = binding.password.editText?.text.toString()
        if(email.isBlank() || email.isBlank() ||
            password.isBlank()){
            hideDialog()
            Toast.makeText(requireContext(),"All fields must be filled",Toast.LENGTH_LONG)
                .show()

        }else{
            if (validateEmail(email)){
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
                   //save user's details in shared pre, navigate to home.
                       val uid = auth.currentUser?.uid!!
                   getUser(uid)
                   hideDialog()
                       findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
               }
           }
           .addOnFailureListener{e->
               Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_LONG).show()
           }
    }

    private fun getUser(uid:String) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(uid)
        userRef.get().addOnSuccessListener {documentSnapshot->
            if(documentSnapshot.exists()) {
                val user = documentSnapshot.toObject(User::class.java)
                if(user !=null){
                    user.name?.let { prefHelper.put(Constant.PREF_NAME, it) }
                    user.email?.let { prefHelper.put(Constant.PREF_EMAIL, it) }
                    prefHelper.put(Constant.PREF_IS_LOGIN,true)
                }

            }
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