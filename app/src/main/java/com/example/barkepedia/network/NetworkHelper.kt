package com.example.barkepedia.network

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import timber.log.Timber

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
        imageView: ImageView,
        placeholderResId: Int
    ) {
        // Early return for empty image ID
        if (referenceImageId.isNullOrEmpty()) {
            imageView.setImageResource(placeholderResId)
            return
        }

        val imageUrl = buildDogImageUrl(referenceImageId)

        val requestOptions = RequestOptions()
            .placeholder(placeholderResId)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .error(placeholderResId)

        Glide.with(context)
            .load(imageUrl)
            .apply(requestOptions)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    Timber.e(e, "Image load failed: ${e?.message}")
                    imageView.setImageResource(placeholderResId)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    Timber.d("Image loaded from: $dataSource")
                    return false
                }
            })
            .into(imageView)
    }

    fun preloadDogImage(context: Context, referenceImageId: String?) {
        if (referenceImageId.isNullOrEmpty()) return

        val imageUrl = buildDogImageUrl(referenceImageId)
        Glide.with(context)
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .preload()
    }

    // Optional: Clear image cache
    fun clearImageCache(context: Context) {
        Glide.get(context).clearMemory()

        Thread {
            Glide.get(context).clearDiskCache()
            Timber.d("Image cache cleared")
        }.start()
    }

    fun loadDogImageWithProgressBar(
        context: Context,
        referenceImageId: String?,
        imageView: ImageView,
        progressBar: ProgressBar,
    ) {
        val imageUrl = buildDogImageUrl(referenceImageId)
        Glide.with(context)
            .load(imageUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    // Hide progress bar when image is loaded from any source
                    progressBar.visibility = View.GONE
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }
            })
            .into(imageView)
    }

}