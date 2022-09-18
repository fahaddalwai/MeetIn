package com.example.meetin.presentation.authscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.meetin.R
import com.example.meetin.databinding.FragmentHomeBinding
import com.example.meetin.databinding.FragmentSplashBinding
import com.example.meetin.presentation.mainscreen.home.HomeViewModel
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


        viewModel.isLoggedIn.observe(viewLifecycleOwner){
            if(it){
                findNavController().navigate(R.id.action_splashFragment_to_signUpFragment)

            }else{

                findNavController().navigate(R.id.action_splashFragment_to_mainActivity)
            }
        }

        return binding.root
    }


}