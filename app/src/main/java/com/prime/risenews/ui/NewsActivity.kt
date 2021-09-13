package com.prime.risenews.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.prime.risenews.NewsRepository
import com.prime.risenews.R
import com.prime.risenews.databinding.ActivityNewsBinding
import com.prime.risenews.db.ArticleDatabase

class NewsActivity : AppCompatActivity() {
    private lateinit var newsBinding: ActivityNewsBinding
    private lateinit var navController: NavController
    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsBinding = DataBindingUtil.setContentView(this, R.layout.activity_news)
        navController = supportFragmentManager
            .findFragmentById(R.id.newsNavHostFragment)?.findNavController()!!


        newsBinding.bottomNavigationView.setupWithNavController(navController)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val newsViewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, newsViewModelProviderFactory).get(NewsViewModel::class.java)

    }
}