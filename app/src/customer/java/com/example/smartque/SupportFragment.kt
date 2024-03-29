package com.example.smartque

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smartque.databinding.FragmentSupportBinding


class SupportFragment : Fragment() {
    private var _binding: FragmentSupportBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSupportBinding.inflate(inflater)
        initViews()
        return binding.root
    }

    private fun initViews() {
        binding.callBtn.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:"+"+254716943633")
            startActivity(callIntent)
        }
    }
}