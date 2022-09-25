package com.isavit.meetin.presentation.authscreen

import android.content.Intent
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
import com.isavit.meetin.core.util.Constants
import com.isavit.meetin.databinding.FragmentSignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: FragmentSignInBinding
    private val viewModel: SigninViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar2.isVisible = it
        }

        viewModel.emailError.observe(viewLifecycleOwner) { msg ->
            if (msg != "") {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.passwordError.observe(viewLifecycleOwner) { msg ->
            if (msg != "") {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.apiError.observe(viewLifecycleOwner) { msg ->
            if (msg != "") {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }

        binding.signInButton.setOnClickListener {
            viewModel.loginUser()
        }


        viewModel.eventGoToHome.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.action_signInFragment_to_mainActivity)
            }
        }

        binding.signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)

        }

        binding.googleSignInButton.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(Constants.GOOGLE_CLIENT_ID)
                .requestEmail()
                .build()
            googleSignInClient =
                activity?.let { fragActivity -> GoogleSignIn.getClient(fragActivity, gso) }!!

            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, Constants.RC_SIGN_IN)
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Constants.RC_SIGN_IN) {
            if (data != null) {
                viewModel.gsoLogin(data)
            }
        }

    }


}