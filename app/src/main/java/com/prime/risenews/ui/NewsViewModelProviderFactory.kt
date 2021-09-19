package com.prime.risenews.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prime.risenews.repository.NewsRepository

class NewsViewModelProviderFactory(
    val app: Application,
    private val newsRepository : NewsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(app, newsRepository) as T
    }
}