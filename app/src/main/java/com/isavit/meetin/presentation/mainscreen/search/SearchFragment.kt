package com.isavit.meetin.presentation.mainscreen.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.isavit.meetin.R
import com.isavit.meetin.databinding.FragmentSearchBinding
import com.isavit.meetin.domain.model.FriendRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar6.isVisible = it
        }


        val adapter = FriendsAdapter(FriendsAdapter.OnClickListener {

            val friendAdded = FriendRequest(
                it.name,
                it.email,
                it.username,
                it.profilePic,
                it.college,
                it.posts
            )
            viewModel.addFriend(
                friendAdded
            )


        })



        binding.friendList.adapter = adapter

        viewModel.userList.observe(viewLifecycleOwner) {
            adapter.submitList(ArrayList(it))
        }

        binding.searchPeople.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {


                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText != null) {
                    viewModel.onSearch(newText)
                }

                return false
            }
        })






        return binding.root
    }

}