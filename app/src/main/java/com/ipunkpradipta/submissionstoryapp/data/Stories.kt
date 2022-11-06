package com.ipunkpradipta.submissionstoryapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Stories(
    var id : String,
    var name: String,
    var description:String,
    var photoUrl:String,
    var createdAt:String,
    var lat : Double,
    var lon : Double
) : Parcelable
