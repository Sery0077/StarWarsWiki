package dev.bonch.starwarswiki.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class Starship {
    data class Pojo (
        val count: Int,
        val next: String?,
        val previous: String?,
        val results: Array<Starship>
    )
    @Parcelize
    data class Starship(
        val name : String,
        val model : String,
        val manufacturer : String,
        val cost_in_credits : String,
        val length : String,
        val max_atmosphering_speed : String,
        val crew : String,
        val passengers : String,
        val cargo_capacity : String,
        val consumables : String,
        val hyperdrive_rating : String,
        val mGLT : String,
        val starship_class : String,
        val pilots : Array<String>,
        val films : Array<String>,
        val created : String,
        val edited : String,
        val url : String
    ): Parcelable
}