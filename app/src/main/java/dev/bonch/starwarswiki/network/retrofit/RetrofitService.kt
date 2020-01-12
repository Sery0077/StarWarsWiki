package dev.bonch.starwarswiki.network.retrofit

import dev.bonch.starwarswiki.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface RetrofitService {

    //Getting lists
    @GET("people/")
    suspend fun getPeoplesList(): Response<People.Pojo>

    @GET("films/")
    suspend fun getFilmsList(): Response<Film.Pojo>

    @GET("planets/")
    suspend fun getPlanetsList(): Response<Planet.Pojo>

    @GET("vehicles/")
    suspend fun getVehiclesList(): Response<Vehicle.Pojo>

    @GET("species/")
    suspend fun getSpeciesList(): Response<Specie.Pojo>

    @GET("starships/")
    suspend fun getStarshipsList(): Response<Starship.Pojo>

    //searching
    @GET("people/")
    suspend fun searchPeople(@Query("search") name: String): Response<People.Pojo>

    @GET("films/")
    suspend fun searchFilm(@Query("search") title: String): Response<Film.Pojo>

    @GET("planets/")
    suspend fun searchPlanet(@Query("search") name: String): Response<Planet.Pojo>

    @GET("vehicles/")
    suspend fun searchVehicle(@Query("search") title: String): Response<Vehicle.Pojo>

    @GET("starships/")
    suspend fun searchStarship(@Query("search") title: String): Response<Starship.Pojo>

    @GET("species/")
    suspend fun searchSpecie(@Query("search") title: String): Response<Specie.Pojo>

    //Getting names
    @GET
    suspend fun getCharactersNames(@Url url: String): Response<People.People>

    @GET
    suspend fun getPlanetsNames(@Url url: String): Response<Planet.Planet>

    @GET
    suspend fun getStarshipsNames(@Url url: String): Response<Starship.Starship>

    @GET
    suspend fun getVehiclesNames(@Url url: String): Response<Vehicle.Vehicle>

    @GET
    suspend fun getSpeciesNames(@Url url: String): Response<Specie.Specie>

    @GET
    suspend fun getFilmsNames(@Url url: String): Response<Film.Film>
}