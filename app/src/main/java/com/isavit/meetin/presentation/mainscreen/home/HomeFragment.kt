package com.isavit.meetin.presentation.mainscreen.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isavit.meetin.R
import com.isavit.meetin.databinding.FragmentHomeBinding
import com.isavit.meetin.presentation.authscreen.AuthActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = HomePostsAdapter()
        binding.postsList.adapter = adapter


        val manager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.postsList.layoutManager = manager

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar7.isVisible = it
        }

        viewModel.logout.observe(viewLifecycleOwner) {
            if (it) {
                val intent = Intent(context, AuthActivity::class.java)
                // start your next activity
                startActivity(intent)
            }
        }

        binding.logoutButton.setOnClickListener {
            viewModel.logOut()
        }
        viewModel.postsList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        return binding.root
    }


    override fun onResume() {
        viewModel.getFriendsPosts()
        super.onResume()
    }


}