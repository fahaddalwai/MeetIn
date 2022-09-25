package com.isavit.meetin.presentation.mainscreen.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.isavit.meetin.databinding.FriendsPostBinding
import com.isavit.meetin.domain.model.Post

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
                binding.captionTextView.text = user.caption

                binding.textView5.text = user.name

                Glide.with(binding.smallProfileImage)
                    .load(user.profileUrl)
                    .fitCenter()
                    .into(binding.smallProfileImage)

                binding.textView9.text = user.university

                Glide.with(binding.smallProfileImage)
                    .load(user.postUrl)
                    .fitCenter()
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