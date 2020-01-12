package dev.bonch.starwarswiki.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class People() {
    data class Pojo (
        val count: Int,
        val next: String?,
        val previous: String?,
        val results: Array<People>
    )
    @Parcelize
    data class People(
        val name: String,
        val birthYear: String,
        val eyeColor: String,
        val gander: String,
        val hairColor: String,
        val height: String,
        val mass: String,
        val skin_color: String,
        val homeworld: String,
        val films: List<String>,
        val species: List<String>,
        val starships: List<String>,
        val vehicles: List<String>,
        val url: String,
        val created: String,
        val edited: String
    ): Parcelable
}