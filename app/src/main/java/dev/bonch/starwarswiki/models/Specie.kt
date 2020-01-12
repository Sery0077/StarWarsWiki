package dev.bonch.starwarswiki.models

import android.os.Parcelable
import dev.bonch.starwarswiki.network.retrofit.RetrofitFactory
import kotlinx.android.parcel.Parcelize
import java.io.IOException

class Specie {
    data class Pojo(
        val count: Int,
        val next: String?,
        val previous: String?,
        val results: Array<Specie>
    )

    @Parcelize
    data class Specie(

        val name: String,
        val classification: String,
        val designation: String,
        val average_height: String,
        val skin_colors: String,
        val hair_colors: String,
        val eye_colors: String,
        val average_lifespan: String,
        val homeworld: String,
        val language: String,
        val people: Array<String>,
        val films: Array<String>,
        val created: String,
        val edited: String,
        val url: String
    ) : Parcelable

    companion object {
        suspend fun factoryDescription(specie: Specie): String {
            var string = ""
            string += "Name: ${specie.name} \n\n"
            string += "Classification: ${specie.classification} \n\n"
            string += "Designation: ${specie.designation} \n\n"
            string += "Average height:  ${specie.average_height} \n\n"
            string += "Skin colors: ${specie.skin_colors} \n\n"
            string += "Hair colors: ${specie.hair_colors} \n\n"
            string += "Eye colors: ${specie.eye_colors} \n\n"
            string += "Average lifespan: ${specie.average_lifespan} \n\n"
            string += "Homeworld: ${specie.homeworld} \n\n"
            string += "Language: ${specie.language} \n\n"
            return string
        }

        suspend fun getSpeciesNames(species: Array<String>): Array<String?> {
            val service = RetrofitFactory.makeRetrofitService()
            var names: Array<String?> = emptyArray()
            for (element in species) {
                names += try {
                    val response = service.getSpeciesNames(element)
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