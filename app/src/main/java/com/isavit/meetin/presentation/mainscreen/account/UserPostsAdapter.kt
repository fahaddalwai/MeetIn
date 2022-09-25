package com.isavit.meetin.presentation.mainscreen.account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.isavit.meetin.databinding.PostItemAccountBinding
import com.isavit.meetin.domain.model.Post

class UserPostsAdapter(val clickListener: PostClickedListener) :
    ListAdapter<Post, UserPostsAdapter.ViewHolder>(PlacesModelDiffCallback()) {

    class ViewHolder private constructor(private val binding: PostItemAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Post, clickListener: PostClickedListener) {

            binding.image = item
            binding.clickListener = clickListener

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PostItemAccountBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val placesModel = getItem(position)!!
        holder.bind(placesModel, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }


}


class PlacesModelDiffCallback : DiffUtil.ItemCallback<Post>() {

    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.postUrl == newItem.postUrl
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}


class PostClickedListener(val clickListener: (postImage: Post) -> Unit) {
    fun onClick(postImage: Post) = clickListener(postImage)
}

