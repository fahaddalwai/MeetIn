package com.example.meetin.presentation.mainscreen.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.meetin.R
import com.example.meetin.databinding.FragmentAccountBinding
import com.example.meetin.databinding.FragmentSetupAccountBinding
import com.example.meetin.presentation.authscreen.SetupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private val viewModel: AccountViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.isLoading.observe(viewLifecycleOwner){
            binding.progressBar4.isVisible=it
        }


        return binding.root
    }


}