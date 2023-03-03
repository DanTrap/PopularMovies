package com.danntrp.movies.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.danntrp.movies.R
import com.danntrp.movies.databinding.MovieItemBinding
import com.danntrp.movies.domain.model.Movie

class MoviePagerAdapter(
    private val onItemClick: (Int) -> Unit,
    private val onItemLongClick: (Movie) -> Unit
) : PagingDataAdapter<Movie, MoviePagerAdapter.MovieViewHolder>(differCallback) {

    inner class MovieViewHolder(private val itemBinding: MovieItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(movie: Movie) {
            itemBinding.apply {
                Glide.with(this.root).load(movie.posterUrlPreview).into(posterImageView)
                movieNameTextView.text = movie.name
                movieGenreYearTextView.text = String.format(
                    this.root.resources.getString(R.string.year_genre),
                    movie.genre.replaceFirstChar { it.uppercase() },
                    movie.year
                )
                favoriteImageView.visibility = if (movie.isFavorite) View.VISIBLE else View.GONE
                constraintLayout.setOnClickListener {
                    onItemClick(movie.id)
                }
                constraintLayout.setOnLongClickListener {
                    favoriteImageView.visibility = View.VISIBLE
                    movie.isFavorite = true
                    onItemLongClick(movie)
                    return@setOnLongClickListener true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        if (movie != null) holder.bind(movie)
    }

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
        }
    }
}