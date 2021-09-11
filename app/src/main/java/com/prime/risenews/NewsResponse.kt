package com.prime.risenews.ui

import com.prime.risenews.Article

data class NewsResponse(
    val totalResults: Int,
    val articles: List<Article>,
    val status: String
)





