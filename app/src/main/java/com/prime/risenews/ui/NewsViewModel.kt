package com.prime.risenews.ui

import androidx.lifecycle.ViewModel
import com.prime.risenews.NewsRepository

class NewsViewModel(
    val repo : NewsRepository
) : ViewModel() {
}