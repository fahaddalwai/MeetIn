package com.example.meetin.core.util

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("profileUrl")
fun bindProfileImage(imgView: ImageView?, imgUrl: String?) {

    imgUrl?.let {
        val imgUri =
            imgUrl.toUri().buildUpon().scheme("https").build()
        imgView?.let {
            Glide.with(imgView.context)
                .load(imgUri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop()
                .into(imgView)
        }

    }

}

@BindingAdapter("profileUrlMain")
fun bindProfileImageMain(imgView: ImageView?, imgUrl: String?) {

    imgUrl?.let {
        val imgUri =
            imgUrl.toUri().buildUpon().scheme("https").build()
        imgView?.let {
            Glide.with(imgView.context)
                .load(imgUri)
                .apply(RequestOptions().override(120, 80))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .into(imgView)
        }

    }

}
