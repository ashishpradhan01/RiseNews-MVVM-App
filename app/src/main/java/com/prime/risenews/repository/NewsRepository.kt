package com.prime.risenews.repository

import com.prime.risenews.api.RetrofitInstance
import com.prime.risenews.db.ArticleDatabase
import com.prime.risenews.models.Article

class NewsRepository(
    val db : ArticleDatabase
) {
    //Networking
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery : String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    //Database
    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun  deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

}