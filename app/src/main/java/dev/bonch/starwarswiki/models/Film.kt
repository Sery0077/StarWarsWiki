package dev.bonch.starwarswiki.models

class Film() {
    data class Pojo (
        val count: Int,
        val next: String?,
        val previous: String?,
        val results: Array<Film>
    )

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
    )
}