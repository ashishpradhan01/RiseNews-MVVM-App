package com.prime.risenews.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.prime.risenews.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article) : Long

    @Query("SELECT * FROM articles")
    //suspend function won't work with livedata
    fun getAllArticles() : LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}