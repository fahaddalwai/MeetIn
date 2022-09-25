package com.isavit.meetin.presentation.mainscreen.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.isavit.meetin.R
import com.isavit.meetin.databinding.FragmentAccountBinding
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

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar4.isVisible = it
        }

        val manager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)

        binding.recyclerView.layoutManager = manager

        viewModel.posts.observe(viewLifecycleOwner) { list ->

            val imagesAdapter = UserPostsAdapter(PostClickedListener() {
                findNavController().navigate(
                    AccountFragmentDirections.actionAccountFragmentToExpandedPostFragment(
                        it.postUrl,
                        it.caption
                    )
                )
            })
            binding.recyclerView.adapter = imagesAdapter
            imagesAdapter.submitList(list)
        }

        binding.addImage.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_postPicFragment)
        }

        return binding.root
    }

    override fun onResume() {
        viewModel.getAccountDetails()
        super.onResume()
    }

}