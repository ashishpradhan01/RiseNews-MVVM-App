package com.prime.risenews.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.prime.risenews.R
import com.prime.risenews.databinding.FragmentArticleBinding
import com.prime.risenews.ui.NewsActivity
import com.prime.risenews.ui.NewsViewModel

class ArticleFragment : Fragment() {
    lateinit var viewModel : NewsViewModel
    private val args : ArticleFragmentArgs by navArgs()
    private lateinit var articleBinding: FragmentArticleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        articleBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_article, container, false)
        return articleBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel

        val article = args.article

        articleBinding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }


    }
}