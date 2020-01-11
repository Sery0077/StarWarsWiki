package dev.bonch.starwarswiki.models

class Vehicle {
    data class Pojo (
        val count: Int,
        val next: String?,
        val previous: String?,
        val results: Array<Vehicle>
    )

    data class Vehicle (

        val name : String,
        val model : String,
        val manufacturer : String,
        val cost_in_credits : Int,
        val length : Double,
        val max_atmosphering_speed : Int,
        val crew : Int,
        val passengers : Int,
        val cargo_capacity : Int,
        val consumables : String,
        val vehicle_class : String,
        val pilots : Array<String>,
        val films : Array<String>,
        val created : String,
        val edited : String,
        val url : String
    )
}