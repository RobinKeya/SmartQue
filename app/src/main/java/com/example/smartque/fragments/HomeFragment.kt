package com.example.smartque.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.smartque.databinding.FragmentHomeBinding
import com.example.smartque.viewmodels.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var viewModel: HomeViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    private val authState = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser
        if(user == null){
            this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews()
        auth = Firebase.auth
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.lifecycleOwner =this
        binding.viewModel = viewModel

        viewModel.value.observe(
            viewLifecycleOwner, Observer
            { navigate ->
                if (navigate != null) {
                    this.findNavController().navigate(
                        HomeFragmentDirections.actionFirstFragmentToSecondFragment(navigate)
                    )
                    viewModel.navigationComplete()
                }
            }
        )
        return binding.root
    }

    private fun initViews() {
        val name = binding.profileName
        val email = binding.profileEmail
        val db = FirebaseFirestore.getInstance()
        val userReference = db.collection("user")
        val uid = auth.currentUser?.uid
        userReference.document(uid!!).get().addOnCompleteListener{task->
            if (task.isSuccessful){
                val data = task.result.data
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(authState)
    }

    override fun onStop() {
        super.onStop()
            FirebaseAuth.getInstance().removeAuthStateListener(authState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}