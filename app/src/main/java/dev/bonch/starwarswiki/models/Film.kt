package dev.bonch.starwarswiki.models

import android.os.Parcelable
import dev.bonch.starwarswiki.network.retrofit.RetrofitFactory
import kotlinx.android.parcel.Parcelize
import java.io.IOException

class Film {
    data class Pojo(
        val count: Int,
        val next: String?,
        val previous: String?,
        val results: Array<Film>
    )

    @Parcelize
    data class Film(
        val title: String,
        val episode_id: Int,
        val opening_crawl: String,
        val director: String,
        val producer: String,
        val release_date: String,
        val characters: Array<String>,
        val planets: Array<String>,
        val starships: Array<String>,
        val vehicles: Array<String>,
        val species: Array<String>,
        val created: String,
        val edited: String,
        val url: String
    ) : Parcelable

    companion object {
        suspend fun factoryDescription(film: Film): String {
            var string = ""
            string += "Title of film: ${film.title} \n\n"
            string += "Episode number: ${film.episode_id} \n\n"
            string += "Opening crawl: ${film.opening_crawl} \n\n"
            string += "Opening crawl: ${film.opening_crawl} \n\n"
            string += "Director: ${film.director} \n\n"
            string += "Producers: ${film.producer} \n\n"
            string += "Release date: ${film.release_date}"
            return string
        }

        suspend fun getFilmsNames(films: Array<String>): Array<String?> {
            val service = RetrofitFactory.makeRetrofitService()
            var names: Array<String?> = emptyArray()
            for (element in films) {
                names += try {
                    val response = service.getFilmsNames(element)
                    if (response.isSuccessful) {
                        response.body()!!.title
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