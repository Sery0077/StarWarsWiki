package dev.bonch.starwarswiki.network.retrofit

import dev.bonch.starwarswiki.models.Film
import dev.bonch.starwarswiki.models.People
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitService {

    @GET("people/")
    suspend fun getPeopleList(): Response<People.Pojo>

    @GET("films/")
    suspend fun getFilmsList(): Response<Film.Pojo>
}