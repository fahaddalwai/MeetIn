package com.example.meetin.presentation.mainscreen.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.meetin.R
import com.example.meetin.databinding.FragmentAccountBinding
import com.example.meetin.databinding.FragmentExpandedPostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpandedPostFragment : Fragment() {
    private lateinit var binding: FragmentExpandedPostBinding
    private val viewModel: ExpandedPostViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_expanded_post, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this



        binding.imageView7.setOnClickListener{
            findNavController().navigate(R.id.action_expandedPostFragment_to_accountFragment)
        }


        return binding.root

    }


}