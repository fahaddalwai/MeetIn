package com.example.meetin.presentation.authscreen

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.meetin.R
import com.example.meetin.databinding.FragmentSetupAccountBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

@AndroidEntryPoint
class SetupAccountFragment : Fragment() {

    private lateinit var binding: FragmentSetupAccountBinding
    private val viewModel: SetupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setup_account, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar3.isVisible=it
        }

        viewModel.nextPage.observe(viewLifecycleOwner) {
            if(it) {
                findNavController().navigate(R.id.action_setupAccountFragment_to_setupAccountUniversityFragment)
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

        viewModel.username.observe(viewLifecycleOwner){
            if(it.toString().isNotEmpty() || it.isNotBlank()){
                viewModel.checkUsernameExists()
            }
        }

        viewModel.isAvailable.observe(viewLifecycleOwner){
            if(it){
                binding.availableText.setColorFilter(ContextCompat.getColor(requireContext(), R.color.tick_mark_green))
            }else{
                binding.availableText.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))

            }
        }

        binding.addImageFab.setOnClickListener{
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }


        binding.selectDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            activity?.let { fragActivity ->
                DatePickerDialog(
                    fragActivity,
                    { _, year, monthOfYear, dayOfMonth ->
                        binding.DobText.setText("$dayOfMonth / $monthOfYear / $year")
                    },
                    year,
                    month,
                    day
                )
            }?.show()
        }


        binding.male.setOnClickListener {
            viewModel.setGenderValue("male")
            binding.male.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.male.setColorFilter(ContextCompat.getColor(requireContext(), R.color.background_white))

            binding.female.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background_white))
            binding.female.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))

            binding.trans.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background_white))
            binding.trans.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
        }

        binding.female.setOnClickListener {
            viewModel.setGenderValue("female")
            binding.female.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.female.setColorFilter(ContextCompat.getColor(requireContext(), R.color.background_white))

            binding.male.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background_white))
            binding.male.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))

            binding.trans.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background_white))
            binding.trans.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
        }

        binding.trans.setOnClickListener {
            viewModel.setGenderValue("transgender")
            binding.trans.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.black))
            binding.trans.setColorFilter(ContextCompat.getColor(requireContext(), R.color.background_white))

            binding.male.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background_white))
            binding.male.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))

            binding.female.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background_white))
            binding.female.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
        }

        binding.nextButton.setOnClickListener{
            viewModel.updateUserDetails()
        }
        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data!!

            val image = data.dataString
            val imageUri = Uri.parse(image)

            binding.profileImageView.setImageURI(imageUri)
            viewModel.addImageToFirebase(imageUri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }


}