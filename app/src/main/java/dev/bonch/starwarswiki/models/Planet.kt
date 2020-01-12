package dev.bonch.starwarswiki.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class Planet {
    data class Pojo (
        val count: Int,
        val next: String?,
        val previous: String?,
        val results: Array<Planet>
    )

    @Parcelize
    data class Planet (
        val name : String,
        val rotation_period : String,
        val orbital_period : String,
        val diameter : String,
        val climate : String,
        val gravity : String,
        val terrain : String,
        val surface_water : String,
        val population : String,
        val residents : Array<String>,
        val films : Array<String>,
        val created : String,
        val edited : String,
        val url : String
    ): Parcelable
}