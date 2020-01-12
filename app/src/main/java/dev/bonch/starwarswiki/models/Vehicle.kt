package dev.bonch.starwarswiki.models

import android.os.Parcelable
import dev.bonch.starwarswiki.network.retrofit.RetrofitFactory
import kotlinx.android.parcel.Parcelize
import java.io.IOException

class Vehicle {
    data class Pojo(
        val count: Int,
        val next: String?,
        val previous: String?,
        val results: Array<Vehicle>
    )

    @Parcelize
    data class Vehicle(

        val name: String,
        val model: String,
        val manufacturer: String,
        val cost_in_credits: String,
        val length: String,
        val max_atmosphering_speed: String,
        val crew: String,
        val passengers: String,
        val cargo_capacity: String,
        val consumables: String,
        val vehicle_class: String,
        val pilots: Array<String>,
        val films: Array<String>,
        val created: String,
        val edited: String,
        val url: String
    ) : Parcelable

    companion object {
        suspend fun factoryDescription(vehicle: Vehicle): String {
            var string = ""
            string += "Name: ${vehicle.name} \n\n"
            string += "Model: ${vehicle.model} \n\n"
            string += "Manufacture: ${vehicle.manufacturer} \n\n"
            string += "Cost in credits:  ${vehicle.cost_in_credits} \n\n"
            string += "Length: ${vehicle.length} \n\n"
            string += "Max atmosphering speed: ${vehicle.max_atmosphering_speed} \n\n"
            string += "Crew: ${vehicle.crew} \n\n"
            string += "Passengers: ${vehicle.passengers} \n\n"
            string += "Cargo capacity: ${vehicle.cargo_capacity} \n\n"
            string += "Consumables: ${vehicle.consumables} \n\n"
            string += "Vehicle class: ${vehicle.vehicle_class} \n\n"
            return string
        }

        suspend fun getVehiclesNames(vehicles: Array<String>): Array<String?> {
            val service = RetrofitFactory.makeRetrofitService()
            var names: Array<String?> = emptyArray()
            for (element in vehicles) {
                names += try {
                    val response = service.getVehiclesNames(element)
                    if (response.isSuccessful) {
                        response.body()!!.name
                    } else {
                        "Error"
                    }
                } catch (err: IOException) {
                    "Error"
                }
            }
            return names
        }
    }
}