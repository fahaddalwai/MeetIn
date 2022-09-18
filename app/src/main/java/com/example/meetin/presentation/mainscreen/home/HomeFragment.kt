package com.example.meetin.presentation.mainscreen.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meetin.R
import com.example.meetin.databinding.FragmentAccountBinding
import com.example.meetin.databinding.FragmentHomeBinding
import com.example.meetin.domain.model.FriendRequest
import com.example.meetin.presentation.mainscreen.account.AccountViewModel
import com.example.meetin.presentation.mainscreen.search.FriendsAdapter
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
        binding.postsList.adapter=adapter


        val manager = LinearLayoutManager(activity, RecyclerView.VERTICAL,false)
        binding.postsList.layoutManager = manager

        viewModel.isLoading.observe(viewLifecycleOwner){
            binding.progressBar7.isVisible=it
        }

        viewModel.logout.observe(viewLifecycleOwner){
            if(it){
                findNavController().navigate(R.id.action_homeFragment_to_authentication_navigation)
            }
        }

        binding.logoutButton.setOnClickListener {
            viewModel.logOut()
        }
        viewModel.postsList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
        return binding.root
    }


    override fun onResume() {
        viewModel.getFriendsPosts()
        super.onResume()
    }


}