package com.prime.risenews.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.prime.risenews.R
import com.prime.risenews.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity() {
    private lateinit var newsBinding: ActivityNewsBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newsBinding = DataBindingUtil.setContentView(this, R.layout.activity_news)
        navController = supportFragmentManager
            .findFragmentById(R.id.newsNavHostFragment)?.findNavController()!!


        newsBinding.bottomNavigationView.setupWithNavController(navController)


    }
}