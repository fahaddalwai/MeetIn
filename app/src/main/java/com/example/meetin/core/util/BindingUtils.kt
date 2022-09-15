package com.example.meetin.core.util

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

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
                .fitCenter()
                .into(imgView)
        }

    }

}

fun RecyclerView.autoFitColumns(columnWidth: Int) {
    val displayMetrics = this.context.resources.displayMetrics
    val noOfColumns = ((displayMetrics.widthPixels / displayMetrics.density) / columnWidth).toInt()
    this.layoutManager = GridLayoutManager(this.context, noOfColumns)
}