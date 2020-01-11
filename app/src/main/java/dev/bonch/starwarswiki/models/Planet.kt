package dev.bonch.starwarswiki.models

class Planet {
    data class Pojo (
        val count: Int,
        val next: String?,
        val previous: String?,
        val results: Array<Planet>
    )

    data class Planet (
        val name : String,
        val rotation_period : Int,
        val orbital_period : Int,
        val diameter : Int,
        val climate : String,
        val gravity : String,
        val terrain : String,
        val surface_water : String,
        val population : Int,
        val residents : Array<String>,
        val films : Array<String>,
        val created : String,
        val edited : String,
        val url : String
    )
}