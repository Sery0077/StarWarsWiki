package dev.bonch.starwarswiki.models

import android.os.Parcelable
import dev.bonch.starwarswiki.network.retrofit.RetrofitFactory
import kotlinx.android.parcel.Parcelize
import java.io.IOException

class People() {
    data class Pojo(
        val count: Int,
        val next: String?,
        val previous: String?,
        val results: Array<People>

    )

    @Parcelize
    data class People(
        val name: String,
        val birth_year: String,
        val eye_color: String,
        val gender: String,
        val hair_color: String,
        val height: String,
        val mass: String,
        val skin_color: String,
        val homeworld: String,
        val films: Array<String>,
        val species: Array<String>,
        val starships: Array<String>,
        val vehicles: Array<String>,
        val url: String,
        val created: String,
        val edited: String
    ) : Parcelable

    companion object {
        suspend fun factoryDescription(peoples: People): String {
            var string = ""
            string += "Name: ${peoples.name} \n\n"
            string += "Birthday year: ${peoples.birth_year} \n\n"
            string += "Eye color: ${peoples.eye_color} \n\n"
            string += "Gender: ${peoples.gender} \n\n"
            string += "Hair color: ${peoples.hair_color} \n\n"
            string += "Height: ${peoples.height} \n\n"
            string += "Mass: ${peoples.mass} \n\n"
            string += "Skin color: ${peoples.skin_color} \n\n"
            string += "Homeworld: ${peoples.homeworld} \n\n"
            return string
        }

        suspend fun getPeoplesNames(characters: Array<String>): Array<String?> {
            val service = RetrofitFactory.makeRetrofitService()
            var names: Array<String?> = emptyArray()
            for (element in characters) {
                names += try {
                    val response = service.getCharactersNames(element)
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