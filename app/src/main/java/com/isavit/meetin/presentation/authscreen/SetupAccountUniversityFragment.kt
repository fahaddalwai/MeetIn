package com.isavit.meetin.presentation.authscreen

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.isavit.meetin.R
import com.isavit.meetin.databinding.FragmentSetupAccountUniversityBinding
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
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_setup_account_university,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progress.isVisible = it
        }

        val collegeOptions = resources.getStringArray(R.array.CollegeOptions)

        val courseOptions = resources.getStringArray(R.array.BranchOptions)

        val spinnerBranch = binding.branchText

        val adapterBranch = context?.let {
            ArrayAdapter(
                it,
                R.layout.spinner_item_list, courseOptions
            )
        }
        spinnerBranch.adapter = adapterBranch

        val spinner = binding.collegeText
        val adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.spinner_item_list, collegeOptions
            )
        }
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                viewModel.college.value = collegeOptions[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        spinnerBranch.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                viewModel.branch.value = courseOptions[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }


        viewModel.nextPage.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.action_setupAccountUniversityFragment_to_mainActivity)
            }
        }

        viewModel.emptyError.observe(viewLifecycleOwner) { msg ->
            if (msg != "") {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.apiError.observe(viewLifecycleOwner) { msg ->
            if (msg != "") {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }

        binding.joinedButton.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            activity?.let { fragActivity ->
                DatePickerDialog(
                    fragActivity,
                    { _, year, _, _ ->
                        binding.joinedYear.text = year.toString()
                    },
                    year,
                    month,
                    day
                )
            }?.show()
        }

        binding.graduationButton.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            activity?.let { fragActivity ->
                DatePickerDialog(
                    fragActivity,
                    { _, year, _, _ ->
                        binding.graduationYear.text = year.toString()
                    },
                    year,
                    month,
                    day
                )
            }?.show()
        }

        binding.nextActivityButton.setOnClickListener {
            viewModel.updateUserDetails()
        }

        return binding.root


    }
}
