package dev.bonch.starwarswiki.models

import android.os.Parcelable
import dev.bonch.starwarswiki.network.retrofit.RetrofitFactory
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.*
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
        fun factoryForView(film: Film): String {
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

        suspend fun getCharactersNames(characters: Array<String>): Array<String?> {
            val service = RetrofitFactory.makeRetrofitService()
            var names: Array<String?> = emptyArray()
            var array: Array<Deferred<String?>> = emptyArray()
            lateinit var job: Deferred<Array<String?>>
            for (element in characters) {
                job = CoroutineScope(Dispatchers.Main).async {
                    array += CoroutineScope(Dispatchers.IO).async {
                        try {
                            val response = service.getCharactersNames(element)
                            if (response.isSuccessful) {
                                return@async response.body()!!.name
                            } else {
                                return@async null
                            }
                        } catch (err: IOException) {
                            return@async null
                        }
                    }
                    for (i in 0..characters.size - 1) names += array[i].await()
                    return@async names
                }
            }
            return job.await()
        }
    }
}