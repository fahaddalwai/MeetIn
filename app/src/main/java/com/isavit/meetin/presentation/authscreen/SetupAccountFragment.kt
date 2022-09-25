package com.isavit.meetin.presentation.authscreen

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.isavit.meetin.R
import com.isavit.meetin.databinding.FragmentSetupAccountBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SetupAccountFragment : Fragment() {

    private lateinit var binding: FragmentSetupAccountBinding
    private val viewModel: SetupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_setup_account, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar3.isVisible = it
        }


        binding.imageView3.setImageResource(R.drawable.male1)
        viewModel.addImageToFirebase(getUri("male1"))
        viewModel.nextPage.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.action_setupAccountFragment_to_setupAccountUniversityFragment)
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

        viewModel.username.observe(viewLifecycleOwner) {
            if (it.toString().isNotEmpty() || it.isNotBlank()) {
                viewModel.checkUsernameExists()
            }
        }

        viewModel.isAvailable.observe(viewLifecycleOwner) {
            if (it) {
                binding.availableText.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.tick_mark_green
                    )
                )
            } else {
                binding.availableText.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )

            }
        }


        binding.imageView4.setOnClickListener {
            binding.imageView3.setImageResource(R.drawable.female1)
            viewModel.addImageToFirebase(getUri("female1"))
        }

        binding.imageView6.setOnClickListener {
            binding.imageView3.setImageResource(R.drawable.female2)
            viewModel.addImageToFirebase(getUri("female2"))

        }

        binding.imageView5.setOnClickListener {
            binding.imageView3.setImageResource(R.drawable.male1)
            viewModel.addImageToFirebase(getUri("male1"))
        }

        binding.imageView10.setOnClickListener {
            binding.imageView3.setImageResource(R.drawable.male2)
            viewModel.addImageToFirebase(getUri("male2"))
        }

        binding.imageView12.setOnClickListener {
            binding.imageView3.setImageResource(R.drawable.male3)
            viewModel.addImageToFirebase(getUri("male3"))
        }

        binding.imageView13.setOnClickListener {
            binding.imageView3.setImageResource(R.drawable.male4)
            viewModel.addImageToFirebase(getUri("male4"))
        }





        binding.selectDate.setOnClickListener {

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)




            context?.let { it1 ->
                val datePickerDialog = DatePickerDialog(
                    it1,
                    { _, year, monthOfYear, dayOfMonth ->
                        binding.DobText.setText("$dayOfMonth / ${monthOfYear + 1} / $year")
                    },
                    year,
                    month,
                    day
                )

                datePickerDialog.datePicker.maxDate = Date().time

                datePickerDialog.show()
            }


        }



        binding.male.setOnClickListener {
            viewModel.setGenderValue("male")
            binding.male.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.male.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.background_white
                )
            )

            binding.female.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.background_white
                )
            )
            binding.female.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))

            binding.trans.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.background_white
                )
            )
            binding.trans.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
        }

        binding.female.setOnClickListener {
            viewModel.setGenderValue("female")
            binding.female.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
            binding.female.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.background_white
                )
            )

            binding.male.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.background_white
                )
            )
            binding.male.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))

            binding.trans.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.background_white
                )
            )
            binding.trans.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
        }

        binding.trans.setOnClickListener {
            viewModel.setGenderValue("transgender")
            binding.trans.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
            binding.trans.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.background_white
                )
            )

            binding.male.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.background_white
                )
            )
            binding.male.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))

            binding.female.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.background_white
                )
            )
            binding.female.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
        }

        binding.nextButton.setOnClickListener {
            viewModel.updateUserDetails()
        }
        return binding.root
    }


    private fun getUri(fileUri: String): Uri {
        return Uri.parse("android.resource://" + context!!.packageName.toString() + "/drawable/$fileUri")
    }


}