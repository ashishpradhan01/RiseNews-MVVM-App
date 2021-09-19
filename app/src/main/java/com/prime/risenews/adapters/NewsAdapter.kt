package com.prime.risenews.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prime.risenews.databinding.ItemArticlePreviewBinding
import com.prime.risenews.models.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder (itemView : ItemArticlePreviewBinding)
        : RecyclerView.ViewHolder(itemView.root)
    {
        val articleImage = itemView.ivArticleImage
        val articleTitle = itemView.tvTitle
        val articleDesc = itemView.tvDescription
        val articlePublishedAt = itemView.tvPublishedAt
        val articleSource = itemView.tvSource
        val noImage = itemView.tvNoImage
    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ItemArticlePreviewBinding.
            inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.apply {
            if(article.urlToImage.isNullOrEmpty()) noImage.visibility = View.VISIBLE
            else {
                try {
                    if (article.urlToImage.contains("http")){
                        noImage.visibility = View.GONE
                        Glide.with(itemView).load(article.urlToImage).into(articleImage)
                    }else{
                        noImage.visibility = View.VISIBLE
                    }
                }catch (e:Exception){
                    noImage.visibility = View.VISIBLE
                }
            }
            articleTitle.text = article.title
            articleDesc.text = article.description
            articlePublishedAt.text = article.publishedAt
            articleSource.text = article.source?.name
            itemView.setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    private var onItemClickListener : ((Article)->Unit)? = null

    fun setItemClickListener(listener : (Article)->Unit){
        onItemClickListener = listener
    }
}

