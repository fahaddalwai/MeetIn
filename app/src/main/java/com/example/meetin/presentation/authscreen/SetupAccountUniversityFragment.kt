package com.example.meetin.presentation.authscreen

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.meetin.R
import com.example.meetin.databinding.FragmentSetupAccountBinding
import com.example.meetin.databinding.FragmentSetupAccountUniversityBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SetupAccountUniversityFragment : Fragment() {

    private lateinit var binding: FragmentSetupAccountUniversityBinding
    private val viewModel: SetupAccountUniversityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setup_account_university, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progress.isVisible=it
        }

        viewModel.nextPage.observe(viewLifecycleOwner) {
            if(it) {
                findNavController().navigate(R.id.action_setupAccountUniversityFragment_to_mainActivity)
            }
        }

        viewModel.emptyError.observe(viewLifecycleOwner) { msg ->
            if (msg != "") {
                Toast.makeText(requireContext(),msg, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.apiError.observe(viewLifecycleOwner) { msg ->
            if (msg != "") {
                Toast.makeText(requireContext(),msg, Toast.LENGTH_SHORT).show()
            }
        }

        binding.joinedButton.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            activity?.let { fragActivity ->
                DatePickerDialog(
                    fragActivity,
                    { _, year, monthOfYear, dayOfMonth ->
                        binding.joinedYear.setText("$dayOfMonth / $monthOfYear / $year")
                    },
                    year,
                    month,
                    day
                )
            }?.show()
        }

        binding.graduationButton.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            activity?.let { fragActivity ->
                DatePickerDialog(
                    fragActivity,
                    { _, year, monthOfYear, dayOfMonth ->
                        binding.graduationYear.setText("$dayOfMonth / $monthOfYear / $year")
                    },
                    year,
                    month,
                    day
                )
            }?.show()
        }

        binding.nextActivityButton.setOnClickListener{
            viewModel.updateUserDetails()
        }

        return binding.root
    }



}
