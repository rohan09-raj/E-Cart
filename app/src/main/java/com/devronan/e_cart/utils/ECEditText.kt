package com.devronan.e_cart.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class ECEditText(context: Context, attributeSet: AttributeSet) :
    AppCompatEditText(context, attributeSet) {

    init {
        //Function to apply font to components
        applyFont()
    }

    private fun applyFont() {
        //Used to get file from assets folder and set to App Name textView
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "GOTHIC.TTF")
        setTypeface(typeface)
    }
}