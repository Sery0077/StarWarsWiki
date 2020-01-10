package dev.bonch.starwarswiki.models

data class Pojo (
    val count: Int? = null,
    val next: String? = null,
    val previous: String? = null,
    val results: Array<People>
)