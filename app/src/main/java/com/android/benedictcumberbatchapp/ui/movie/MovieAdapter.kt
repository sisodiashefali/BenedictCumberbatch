package com.android.benedictcumberbatchapp.ui.movie

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.benedictcumberbatchapp.data.model.Result
import com.android.benedictcumberbatchapp.databinding.ViewHolderMovieBinding

class MovieAdapter : PagingDataAdapter<Result, MovieAdapter.CustomViewHolder>(DIFF_UTIL) {

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class CustomViewHolder(val viewDataBinding: ViewHolderMovieBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {

    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        holder.viewDataBinding.movie = getItem(position)

        holder.viewDataBinding.movieCardView.setOnClickListener {
            onCardClickListener?.let {
                getItem(position)?.let {
                    result -> it(result)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val binding = ViewHolderMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CustomViewHolder(binding)
    }


    private var onCardClickListener : ((Result) -> Unit)? = null

    fun setMovieDetailListener(onClickListener : (Result) -> Unit){
        onCardClickListener = onClickListener
    }


}