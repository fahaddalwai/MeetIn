package com.isavit.meetin.presentation.authscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.isavit.meetin.R
import com.isavit.meetin.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private val viewModel: SplashViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)

        binding.lifecycleOwner = this


        viewModel.isLoggedIn.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.action_splashFragment_to_mainActivity)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_signUpFragment)
            }
        }

        return binding.root
    }


}