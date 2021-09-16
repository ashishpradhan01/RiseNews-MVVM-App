package com.prime.risenews.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.prime.risenews.R
import com.prime.risenews.adapters.NewsAdapter
import com.prime.risenews.databinding.FragmentSearchNewsBinding
import com.prime.risenews.ui.NewsActivity
import com.prime.risenews.ui.NewsViewModel
import com.prime.risenews.utils.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.prime.risenews.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchNewsFragment : Fragment(){
    lateinit var viewModel: NewsViewModel
    private lateinit var searchNewsBinding: FragmentSearchNewsBinding
    private lateinit var newsAdapter : NewsAdapter

    val TAG = "SearchNewsFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchNewsBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_search_news,container, false)
        return searchNewsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        newsAdapter.setItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel = (activity as NewsActivity).viewModel

        var job : Job? = null
        searchNewsBinding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                        viewModel.getSearchNews(editable.toString())
                    }
                }
            }
        }


        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.d(TAG, "An error occurred: $message")
                    }
                }
            }

        })


    }

    private fun hideProgressBar(){
        searchNewsBinding.paginationProgressBar.visibility = View.GONE
    }

    private fun showProgressBar(){
        searchNewsBinding.paginationProgressBar.visibility = View.VISIBLE
    }



    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        searchNewsBinding.rvSearchNews.apply {
            adapter = newsAdapter
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(activity)
        }
    }
}