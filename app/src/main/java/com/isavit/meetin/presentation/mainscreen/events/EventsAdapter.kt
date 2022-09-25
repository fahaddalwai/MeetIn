package com.isavit.meetin.presentation.mainscreen.events

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.isavit.meetin.databinding.EventListItemBinding
import com.isavit.meetin.domain.model.Event
import kotlinx.android.synthetic.main.event_list_item.view.*

class EventsAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Event, EventsAdapter.MyViewHolder>(MyDiffUtil) {

    companion object MyDiffUtil : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.eventName == newItem.eventName
        }
    }

    inner class MyViewHolder(private val binding: EventListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event) {
            Glide.with(binding.smallProfileImage)
                .load(event.eventImage)
                .fitCenter()
                .into(binding.smallProfileImage)

            binding.textView5.text = event.eventName

            binding.textView8.text = event.eventTime + " " + event.eventDate

            binding.textView9.text = "By " + event.eventBy

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            EventListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.itemView.shareLocation.setOnClickListener {
            onClickListener.onClickLocation(event)
        }

        holder.itemView.shareIcon.setOnClickListener {
            onClickListener.onClickShare(event)
        }

        holder.bind(event)
    }

    class OnClickListener(
        val clickListenerLocation: (event: Event) -> Unit,
        val clickListenerShare: (event: Event) -> Unit
    ) {
        fun onClickLocation(user: Event) = clickListenerLocation(user)
        fun onClickShare(user: Event) = clickListenerShare(user)
    }
}