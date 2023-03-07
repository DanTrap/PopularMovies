package com.danntrp.movies.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.danntrp.movies.databinding.LoadStateBinding

class LoadingStateAdapter :
    LoadStateAdapter<LoadingStateAdapter.StateViewHolder>() {

    override fun onBindViewHolder(
        holder: StateViewHolder,
        loadState: LoadState
    ) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): StateViewHolder {
        return StateViewHolder(
            LoadStateBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    class StateViewHolder(binding: LoadStateBinding) :
        RecyclerView.ViewHolder(binding.root)
}