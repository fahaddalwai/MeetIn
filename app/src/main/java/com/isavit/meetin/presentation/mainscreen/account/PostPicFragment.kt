package com.isavit.meetin.presentation.mainscreen.account

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.isavit.meetin.R
import com.isavit.meetin.databinding.FragmentPostPicBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostPicFragment : Fragment() {

    private lateinit var binding: FragmentPostPicBinding
    private val viewModel: PostPicViewModel by viewModels()

    var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_pic, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar5.isVisible = it
        }


        binding.changeUpload.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    480,
                    480
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }



        binding.postButton.setOnClickListener {
            if (imageUri != null) {
                viewModel.addImageToFirebase(imageUri!!)
            } else {
                Toast.makeText(requireContext(), "Please Upload an image first", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        viewModel.apiError.observe(viewLifecycleOwner) { msg ->
            if (msg != "") {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.captionEmpty.observe(viewLifecycleOwner) { msg ->
            if (msg != "") {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.goBackAccount.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.action_postPicFragment_to_accountFragment)
            }
        }


        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_postPicFragment_to_accountFragment)
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val uri: Uri = data?.data!!

                val image = data.dataString
                imageUri = Uri.parse(image)
                binding.uploadText.isVisible = false

                binding.imagePost.setImageURI(imageUri)
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }


}