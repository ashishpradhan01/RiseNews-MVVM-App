package com.prime.risenews
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.prime.risenews.ui.Source

@Entity(
    tableName = "articles"
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id:Int? = null,
    val publishedAt: String,
    val author: String,
    val urlToImage: String,
    val description: String,
    val source: Source,
    val title: String,
    val url: String,
    val content: String
)