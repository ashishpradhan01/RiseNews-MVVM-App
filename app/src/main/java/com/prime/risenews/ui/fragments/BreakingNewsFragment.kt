package com.prime.risenews.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.prime.risenews.R
import com.prime.risenews.adapters.NewsAdapter
import com.prime.risenews.databinding.FragmentBreakingNewsBinding
import com.prime.risenews.ui.NewsActivity
import com.prime.risenews.ui.NewsViewModel
import com.prime.risenews.utils.Resource

class BreakingNewsFragment : Fragment(){
    lateinit var viewModel: NewsViewModel
    lateinit var breakingNewsBinding: FragmentBreakingNewsBinding
    lateinit var newsAdapter: NewsAdapter

    val TAG = "BreakingNewsFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        breakingNewsBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_breaking_news, container, false)
        return breakingNewsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {  response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }


    private fun hideProgressBar(){
        breakingNewsBinding.paginationProgressBar.visibility = View.GONE
    }

    private fun showProgressBar(){
        breakingNewsBinding.paginationProgressBar.visibility = View.VISIBLE
    }



    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        breakingNewsBinding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}