package com.devronan.e_cart.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    val user_id: String = "",
    val name: String = "",
    val mobile_number: String = "",
    val address: String = "",
    val pin_code: String = "",
    var additional_note: String = "",
    var type: String = "",
    var other_details: String = "",
    var id: String = ""
) : Parcelable