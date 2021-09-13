package com.prime.risenews.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.prime.risenews.R
import com.prime.risenews.ui.NewsActivity
import com.prime.risenews.ui.NewsViewModel

class SearchNewsFragment : Fragment(R.layout.fragment_search_news){
    lateinit var viewModel: NewsViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel
    }
}