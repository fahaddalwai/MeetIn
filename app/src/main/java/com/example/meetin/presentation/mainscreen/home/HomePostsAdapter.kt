package com.example.meetin.presentation.mainscreen.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meetin.databinding.FriendItemBinding
import com.example.meetin.databinding.FriendsPostBinding

import com.example.meetin.domain.model.FriendRequest
import com.example.meetin.domain.model.Post
import com.example.meetin.domain.model.UserDetailsRequest

import kotlinx.android.synthetic.main.friend_item.view.*

class HomePostsAdapter() :
    ListAdapter<Post, HomePostsAdapter.MyViewHolder>(MyDiffUtil) {

    companion object MyDiffUtil : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(
            oldItem: Post,
            newItem: Post
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Post,
            newItem: Post
        ): Boolean {
            return oldItem.postUrl == newItem.postUrl
        }
    }

    inner class MyViewHolder(private val binding: FriendsPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: Post?) {

            if (user != null) {
                binding.captionTextView.text= user.caption
            }
            if (user != null) {
                binding.textView5.text=user.name
            }

            if (user != null) {
                Glide.with(binding.smallProfileImage)
                    .load(user.profileUrl)
                    .centerCrop()
                    .into(binding.smallProfileImage)
            }
            if (user != null) {
                binding.textView9.text=user.university
            }

            if (user != null) {
                Glide.with(binding.smallProfileImage)
                    .load(user.postUrl)
                    .centerCrop()
                    .into(binding.imageView11)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            FriendsPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val meme = getItem(position)
        holder.bind(meme)
    }


}