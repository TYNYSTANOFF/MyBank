package com.example.mybank.data.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Account(
    val id : String?= null,
    val name : String?= null,
    val currency : String?= null,
    val balance : Int?= null,
    val isActive : Boolean?= null
): Serializable
