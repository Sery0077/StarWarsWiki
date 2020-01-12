package dev.bonch.starwarswiki.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class Specie {
    data class Pojo (
        val count: Int,
        val next: String?,
        val previous: String?,
        val results: Array<Specie>
    )
    @Parcelize
    data class Specie (

        val name : String,
        val classification : String,
        val designation : String,
        val average_height : String,
        val skin_colors : String,
        val hair_colors : String,
        val eye_colors : String,
        val average_lifespan : String,
        val homeworld : String,
        val language : String,
        val people : Array<String>,
        val films : Array<String>,
        val created : String,
        val edited : String,
        val url : String
    ): Parcelable
}