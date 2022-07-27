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
import com.example.smartque.helper.Constant
import com.example.smartque.helper.PrefHelper
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
    private lateinit var prefHelper: PrefHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.lifecycleOwner =this
        binding.viewModel = viewModel
        prefHelper = PrefHelper(requireContext())
        initViews()
        viewModel.value.observe(
            viewLifecycleOwner,
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
        // read from sharedPreference
        name.text= prefHelper.getString(Constant.PREF_NAME)
        email.text = prefHelper.getString(Constant.PREF_EMAIL)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}