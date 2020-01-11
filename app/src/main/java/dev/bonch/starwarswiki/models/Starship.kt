package dev.bonch.starwarswiki.models

class Starship {
    data class Pojo (
        val count: Int,
        val next: String?,
        val previous: String?,
        val results: Array<Starship>
    )

    data class Starship(
        val name : String,
        val model : String,
        val manufacturer : String,
        val cost_in_credits : Int,
        val length : Int,
        val max_atmosphering_speed : String,
        val crew : Int,
        val passengers : Int,
        val cargo_capacity : Int,
        val consumables : String,
        val hyperdrive_rating : Double,
        val mGLT : Int,
        val starship_class : String,
        val pilots : Array<String>,
        val films : Array<String>,
        val created : String,
        val edited : String,
        val url : String
    )
}