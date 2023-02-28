package com.danntrp.movies.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.danntrp.movies.databinding.LoadStateBinding

class LoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingStateAdapter.StateViewHolder>() {

    override fun onBindViewHolder(
        holder: StateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): StateViewHolder {
        return StateViewHolder(
            LoadStateBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            retry
        )
    }

    class StateViewHolder(private val binding: LoadStateBinding, private val retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            Log.d("ABOBA", "Bind: $loadState")
            binding.contentProgressBar.isVisible =
                loadState is LoadState.Loading || loadState is LoadState.Error
        }
    }
}