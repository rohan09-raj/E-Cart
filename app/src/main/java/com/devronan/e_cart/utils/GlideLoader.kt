package com.devronan.e_cart.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.devronan.e_cart.R
import java.io.IOException

class GlideLoader(val context: Context) {

    fun loadUserPicture(image: Any, imageView: ImageView) {
        try {
            //Load the user image in the imageView
            Glide
                .with(context)
                .load(image) //Uri of the image
                .centerCrop() //Scale type of the image
                .placeholder(R.drawable.ic_user_placeholder) //Placeholder for the imageView
                .into(imageView) //The view in which the image will be loaded
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadProductPicture(image: Any, imageView: ImageView) {
        try {
            //Load the user image in the imageView
            Glide
                .with(context)
                .load(image) //Uri of the image
                .centerCrop() //Scale type of the image
                .into(imageView) //The view in which the image will be loaded
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}