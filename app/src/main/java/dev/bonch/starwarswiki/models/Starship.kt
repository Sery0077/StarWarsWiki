package dev.bonch.starwarswiki.models

import android.os.Parcelable
import dev.bonch.starwarswiki.network.retrofit.RetrofitFactory
import kotlinx.android.parcel.Parcelize
import java.io.IOException

class Starship {
    data class Pojo(
        val count: Int,
        val next: String?,
        val previous: String?,
        val results: Array<Starship>
    )

    @Parcelize
    data class Starship(
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
        val hyperdrive_rating: String,
        val mGLT: String,
        val starship_class: String,
        val pilots: Array<String>,
        val films: Array<String>,
        val created: String,
        val edited: String,
        val url: String
    ) : Parcelable

    companion object {
        suspend fun factoryDescription(starships: Starship): String {
            var string = ""
            string += "Name: ${starships.name} \n\n"
            string += "Model: ${starships.model} \n\n"
            string += "Manufacture: ${starships.manufacturer} \n\n"
            string += "Cost in credits:  ${starships.cost_in_credits} \n\n"
            string += "Length: ${starships.length} \n\n"
            string += "Max atmosphering speed: ${starships.max_atmosphering_speed} \n\n"
            string += "Crew: ${starships.crew} \n\n"
            string += "Passengers: ${starships.passengers} \n\n"
            string += "Cargo capacity: ${starships.cargo_capacity} \n\n"
            string += "Consumables: ${starships.consumables} \n\n"
            string += "Hyperdrive rating: ${starships.hyperdrive_rating} \n\n"
            string += "MGLT: ${starships.mGLT} \n\n"
            string += "Starship class: ${starships.starship_class} \n\n"
            return string
        }

        suspend fun getStarshipsNames(starships: Array<String>): Array<String?> {
            val service = RetrofitFactory.makeRetrofitService()
            var names: Array<String?> = emptyArray()
            for (element in starships) {
                names += try {
                    val response = service.getStarshipsNames(element)
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