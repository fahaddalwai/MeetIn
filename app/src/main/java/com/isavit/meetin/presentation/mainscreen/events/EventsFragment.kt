package com.isavit.meetin.presentation.mainscreen.events

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.isavit.meetin.R
import com.isavit.meetin.databinding.FragmentEventsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EventsFragment : Fragment() {
    private lateinit var binding: FragmentEventsBinding
    private val viewModel: EventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_events, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.eventsProgressBar.isVisible = it
        }

        val adapter = EventsAdapter(EventsAdapter.OnClickListener({
            val url = "https://www.google.com/maps/search/?api=1&query=${it.eventLocation}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }, {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "${it.eventName} is happening at ${it.eventLocation} on the ${it.eventDate}! Thought you should join me too!"
            )
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share Via"))
        }))

        binding.eventList.adapter = adapter


        viewModel.eventList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }


}