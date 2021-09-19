package com.prime.risenews.models

data class NewsResponse(
    val totalResults: Int,
    val articles: MutableList<Article>,
    val status: String
)





