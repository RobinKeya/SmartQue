package com.example.smartque

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.smartque.databinding.FragmentDetailsBinding
import com.example.smartque.details.DetailsViewModel
import com.example.smartque.details.DetailsViewModelFactory
import com.example.smartque.helper.Constant
import com.example.smartque.helper.PrefHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailsFragment : Fragment() {
    private lateinit var viewModel: DetailsViewModel
    private lateinit var viewModelFactory: DetailsViewModelFactory
    private var _binding: FragmentDetailsBinding? = null
    private lateinit var prefHelper: PrefHelper

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        prefHelper = PrefHelper(requireContext())
        binding.name.text = prefHelper.getString(Constant.PREF_NAME)
        val app = requireNotNull(activity).application!!
        val args = DetailsFragmentArgs.fromBundle(requireArguments()).value
        viewModelFactory = DetailsViewModelFactory(args,app)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        initViews()
        return binding.root

    }

    private fun initViews() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Alert")
            .setMessage("Removed from Queue?")
            .setNegativeButton("No"){ dialog, _ ->dialog.cancel()}
            .setPositiveButton("Yes"){ _, _ ->
                findNavController().navigate(DetailsFragmentDirections.actionSecondFragmentToFirstFragment())
            }
        val rand = Random()
        val value = viewModel.value
        val ticket = "${rand.nextInt(16)} + ${when(value){
            0->"W"
            1->"D"
            2->"L"
            3->"O"
            4->"Q"
            else->"I"

        }        }"
        val numInQ = rand.nextInt(5)
        val time = numInQ.times(2)

        binding.ticketNumber.text = ticket
        binding.numberInQ.text = numInQ.toString()
        binding.timeInQ.text = time.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}