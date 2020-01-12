package dev.bonch.starwarswiki.models

import android.os.Parcelable
import dev.bonch.starwarswiki.network.retrofit.RetrofitFactory
import kotlinx.android.parcel.Parcelize
import java.io.IOException

class Planet {
    data class Pojo(
        val count: Int,
        val next: String?,
        val previous: String?,
        val results: Array<Planet>
    )

    @Parcelize
    data class Planet(
        val name: String,
        val rotation_period: String,
        val orbital_period: String,
        val diameter: String,
        val climate: String,
        val gravity: String,
        val terrain: String,
        val surface_water: String,
        val population: String,
        val residents: Array<String>,
        val films: Array<String>,
        val created: String,
        val edited: String,
        val url: String
    ) : Parcelable

    companion object {
        suspend fun factoryDescription(planet: Planet): String {
            var string = ""
            string += "Name: ${planet.name} \n\n"
            string += "Rotation period: ${planet.rotation_period} \n\n"
            string += "Orbital period: ${planet.orbital_period} \n\n"
            string += "Diameter: ${planet.diameter} \n\n"
            string += "Climate: ${planet.climate} \n\n"
            string += "Gravity: ${planet.gravity} \n\n"
            string += "Terrain: ${planet.terrain} \n\n"
            string += "Surface water: ${planet.surface_water} \n\n"
            string += "Population: ${planet.population} \n\n"
            return string
        }

        suspend fun getPlanetsNames(planets: Array<String>): Array<String?> {
            val service = RetrofitFactory.makeRetrofitService()
            var names: Array<String?> = emptyArray()
            for (element in planets) {
                names += try {
                    val response = service.getPlanetsNames(element)
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