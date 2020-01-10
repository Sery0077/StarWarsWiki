package dev.bonch.starwarswiki.network.retrofit

import dev.bonch.starwarswiki.models.Pojo
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitService {

    @GET("people/")
    suspend fun getPeopleList(): Response<Pojo>
}