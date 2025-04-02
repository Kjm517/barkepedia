package com.example.barkepedia.network

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.barkepedia.R
import com.example.barkepedia.view.main.DogBreedDetailActivity

object NetworkHelper {
    private const val DOG_API_IMAGE_BASE_URL = "https://cdn2.thedogapi.com/images/"
    private const val DEFAULT_IMAGE_URL = "https://example.com/default_image.jpg"

    fun buildDogImageUrl(referenceImageId: String?): String {
        return if (referenceImageId.isNullOrEmpty()) {
            DEFAULT_IMAGE_URL
        } else {
            "$DOG_API_IMAGE_BASE_URL$referenceImageId.jpg"
        }
    }

    fun loadDogImage(
        context: Context,
        referenceImageId: String?,
        imageView: ImageView )
    {
        val imageUrl = buildDogImageUrl(referenceImageId)
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(imageView)
    }

    fun loadDogImage(
        itemView: View,
        referenceImageId: String?,
        imageView: ImageView,
        progressBar: ProgressBar? = null
    ) {
        if (referenceImageId.isNullOrEmpty()) {
            progressBar?.visibility = View.GONE
            return
        }

        val imageUrl = buildDogImageUrl(referenceImageId)

        progressBar?.visibility = View.VISIBLE

        Glide.with(itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar?.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar?.visibility = View.GONE
                    return false
                }
            })
            .into(imageView)
    }

//    fun preloadDogImage(context: Context, referenceImageId: String?) {
//        if (referenceImageId.isNullOrEmpty()) return
//
//        val imageUrl = buildDogImageUrl(referenceImageId)
//        Glide.with(context)
//            .load(imageUrl)
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .preload()
//    }
}