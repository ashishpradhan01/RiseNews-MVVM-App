package com.prime.risenews.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prime.risenews.R
import com.prime.risenews.adapters.NewsAdapter
import com.prime.risenews.databinding.FragmentBreakingNewsBinding
import com.prime.risenews.ui.NewsActivity
import com.prime.risenews.ui.NewsViewModel
import com.prime.risenews.utils.Constants.Companion.CANADA_COUNTRY_CODE
import com.prime.risenews.utils.Constants.Companion.CHINA_COUNTRY_CODE
import com.prime.risenews.utils.Constants.Companion.INDIA_COUNTRY_CODE
import com.prime.risenews.utils.Constants.Companion.JAPAN_COUNTRY_CODE
import com.prime.risenews.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.prime.risenews.utils.Constants.Companion.UNITED_STATES_COUNTRY_CODE
import com.prime.risenews.utils.Resource

class BreakingNewsFragment : Fragment() {
    lateinit var viewModel: NewsViewModel
    private lateinit var breakingNewsBinding: FragmentBreakingNewsBinding
    lateinit var newsAdapter: NewsAdapter

    val TAG = "BreakingNewsFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        breakingNewsBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_breaking_news, container, false
        )
        return breakingNewsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if (isLastPage) {
                            breakingNewsBinding.rvBreakingNews
                                .setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(
                            activity, "An error occurred: $message",
                            Toast.LENGTH_LONG
                        ).show()
                        showErrorMessage(message)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        breakingNewsBinding.itemErrorMessage.findViewById<Button>(R.id.btnRetry).setOnClickListener {
                viewModel.getBreakingNews(viewModel.countryCurrentCode)
        }

        breakingNewsBinding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chipIndia -> viewModel.setCountryCode(INDIA_COUNTRY_CODE)
                R.id.chipUs -> viewModel.setCountryCode(UNITED_STATES_COUNTRY_CODE)
                R.id.chipJapan -> viewModel.setCountryCode(JAPAN_COUNTRY_CODE)
                R.id.chipCanada -> viewModel.setCountryCode(CANADA_COUNTRY_CODE)
                R.id.chipChina -> viewModel.setCountryCode(CHINA_COUNTRY_CODE)
            }
            viewModel.apply {
                breakingNewsPage = 1
                breakingNewsResponse = null
                getBreakingNews(countryCurrentCode)
            }
        }

        breakingNewsBinding.apply {
            when(viewModel.countryCurrentCode){
                INDIA_COUNTRY_CODE -> chipIndia.isChecked = true
                UNITED_STATES_COUNTRY_CODE -> chipUs.isChecked = true
                JAPAN_COUNTRY_CODE -> chipJapan.isChecked = true
                CANADA_COUNTRY_CODE -> chipCanada.isChecked = true
                CHINA_COUNTRY_CODE -> chipChina.isChecked = true
            }
        }

    }

    private fun hideProgressBar(){
        breakingNewsBinding.paginationProgressBar.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar(){
        breakingNewsBinding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideErrorMessage() {
        breakingNewsBinding.itemErrorMessage.visibility = View.INVISIBLE
        isError = false
    }

    private fun showErrorMessage(message: String) {
        breakingNewsBinding.apply {
            itemErrorMessage.visibility = View.VISIBLE
            itemErrorMessage.findViewById<TextView>(R.id.tvErrorMessage).apply {
                text =  if (message.isNotEmpty()) message else getString(R.string.failed_to_connect)
            }
        }
        isError = true
    }

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling &&isNoErrors
            if (shouldPaginate){
                viewModel.getBreakingNews(viewModel.countryCurrentCode)
                isScrolling = false
            }
        }
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        breakingNewsBinding.rvBreakingNews.apply {
            adapter = newsAdapter
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(activity)
        }
    }
}