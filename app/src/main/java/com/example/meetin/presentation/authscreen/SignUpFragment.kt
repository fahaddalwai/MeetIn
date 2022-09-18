package com.example.meetin.presentation.authscreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.meetin.R
import com.example.meetin.core.util.Constants
import com.example.meetin.core.util.Constants.GOOGLE_CLIENT_ID
import com.example.meetin.core.util.Constants.RC_SIGN_IN

import com.example.meetin.databinding.FragmentSignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        viewModel.isLoading.observe(viewLifecycleOwner){
            binding.progressBar.isVisible=it
        }


        viewModel.emailError.observe(viewLifecycleOwner) {
            if(it!="") {
                    Toast.makeText(requireContext(),it,LENGTH_SHORT).show()
            }
        }

        viewModel.passwordError.observe(viewLifecycleOwner) { msg ->
            if(msg!="") {
                Toast.makeText(requireContext(),msg,LENGTH_SHORT).show()
            }
        }


        viewModel.passwordMisMatchError.observe(viewLifecycleOwner) { msg ->
            if(msg!="") {
                Toast.makeText(requireContext(),msg,LENGTH_SHORT).show()
            }
        }




        viewModel.password.observe(viewLifecycleOwner){ password ->
            viewModel.repeatedPassword.observe(viewLifecycleOwner){ repeatedPassword ->
                if(repeatedPassword==password){
                    binding.correctMatch.setColorFilter(ContextCompat.getColor(requireContext(), R.color.tick_mark_green))
                }else{
                    binding.correctMatch.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black))
                }
            }
        }


        binding.signupButton.setOnClickListener{
            viewModel.signInUser()
        }


        viewModel.eventGoToSetup.observe(viewLifecycleOwner){
            if(it){
                findNavController().navigate(R.id.action_signUpFragment_to_setupAccountFragment)
            }
        }

        binding.signinButton.setOnClickListener{
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)

        }

        binding.signupWGoogle.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(GOOGLE_CLIENT_ID)
                .requestEmail()
                .build()
            googleSignInClient =
                activity?.let { fragActivity -> GoogleSignIn.getClient(fragActivity, gso) }!!

            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)

        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            if (data != null) {
                viewModel.gsoLogin(data)
            }
        }

    }


}