package com.example.smartque

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.smartque.databinding.FragmentSignUpBinding
import com.example.smartque.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
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
    ): View {
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
        showDialog()
        val name = binding.profileName.editText?.text.toString()
        val email = binding.email.editText?.text.toString()
        val password = binding.password.editText?.text.toString()
        val confirmPassword= binding.confirmPassword.editText?.text.toString()
        if(name.isBlank() || email.isBlank() || email.isBlank() ||
            password.isBlank() || confirmPassword.isBlank()){
                hideDialog()
            Toast.makeText(requireContext(),"All fields must be filled",Toast.LENGTH_LONG)
                .show()
        }else{
            if (passwordMatch(password, confirmPassword) && validateEmail(email)){
                //save user, in the db then sign in.
                registerUser(name,email, password)
            }else{
                Toast.makeText(requireContext(),"Password doen't match",Toast.LENGTH_SHORT).show()
                hideDialog()
            }
        }

    }

    private fun registerUser(name: String,email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if(task.isSuccessful){
                    sendVerificationEmail()
                    saveUser(name, email)
                }
            }
            .addOnFailureListener{e->
                Toast.makeText(requireContext(), e.localizedMessage,Toast.LENGTH_LONG).show()
            }
    }

    private fun saveUser(name: String, email:String) {
        val db =FirebaseFirestore.getInstance()
        val userReference = db.collection("user")
        val uid = if(auth.currentUser != null){
            auth.currentUser?.uid
        }else null

        val user = User(uid,name,email)
        userReference.document(uid!!).set(user)
            .addOnCompleteListener{ task->
                if (task.isSuccessful){
                    Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                    hideDialog()
                    FirebaseAuth.getInstance().signOut()
                    this.findNavController().navigate(SignUpFragmentDirections
                        .actionSignUpFragmentToLoginFragment())
                }else{
                    Toast.makeText(requireContext(),"Failed to Sign up Users",Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{e->
                hideDialog()
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(requireContext(), e.localizedMessage,Toast.LENGTH_LONG).show()
            }
    }

    private fun passwordMatch(password: String, confirmPassword: String):Boolean{
        return password == confirmPassword
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
    private fun sendVerificationEmail(){
        val user =auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener(){ task->
            if(task.isSuccessful){
                //Todo -> Implicit intent to gmail or other mailing services.
                Toast.makeText(requireContext(),"Email verification Sent",
                    Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(requireContext(), "Failed to send Verification link",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

}