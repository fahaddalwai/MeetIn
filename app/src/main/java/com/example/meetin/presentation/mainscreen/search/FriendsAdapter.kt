package com.example.meetin.presentation.mainscreen.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meetin.databinding.FriendItemBinding
import com.example.meetin.domain.model.UserDetailsRequest
import kotlinx.android.synthetic.main.friend_item.view.*

class FriendsAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<UserDetailsRequest, FriendsAdapter.MyViewHolder>(MyDiffUtil) {

    companion object MyDiffUtil : DiffUtil.ItemCallback<UserDetailsRequest>() {
        override fun areItemsTheSame(oldItem: UserDetailsRequest, newItem: UserDetailsRequest): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UserDetailsRequest, newItem: UserDetailsRequest): Boolean {
            return oldItem.email == newItem.email
        }
    }

    inner class MyViewHolder(private val binding: FriendItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserDetailsRequest?) {
            if (user != null) {
                Glide.with(binding.smallProfileImage)
                    .load(user.profilePic)
                    .centerCrop()
                    .into(binding.smallProfileImage)
            }

            if (user != null) {
                binding.textView5.text=user.username
            }

            if (user != null) {
                binding.textView8.text=user.college
            }

            if (user != null) {
                binding.textView9.text=user.branch
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            FriendItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val meme = getItem(position)
        holder.itemView.imageView9.setOnClickListener {
            onClickListener.onClick(meme)
        }




        holder.bind(meme)
    }

    class OnClickListener(val clickListener: (user: UserDetailsRequest) -> Unit) {
        fun onClick(user: UserDetailsRequest) = clickListener(user)
    }
}