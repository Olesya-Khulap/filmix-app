package com.example.filmix.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadImage(
    url: String?,
    placeholder: Int = android.R.color.darker_gray,
    error: Int = android.R.color.darker_gray,
    cornerRadius: Int = 0
) {
    val requestOptions = RequestOptions()
        .placeholder(placeholder)
        .error(error)

    if (cornerRadius > 0) {
        requestOptions.transform(RoundedCorners(cornerRadius))
    }

    Glide.with(this.context)
        .load(url)
        .apply(requestOptions)
        .into(this)
}

fun String?.toImageUrl(size: String = Constants.POSTER_SIZE_W342): String? {
    return if (this.isNullOrEmpty()) null
    else "${Constants.IMAGE_BASE_URL}$size$this"
}

fun Double.formatRating(): String {
    return String.format("%.1f", this)
}
