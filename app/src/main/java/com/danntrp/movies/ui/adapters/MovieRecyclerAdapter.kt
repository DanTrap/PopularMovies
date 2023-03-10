package com.danntrp.movies.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.danntrp.movies.R
import com.danntrp.movies.databinding.MovieItemBinding
import com.danntrp.movies.domain.model.Movie

class MovieRecyclerAdapter : RecyclerView.Adapter<MovieRecyclerAdapter.MovieViewHolder>() {

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
                    onItemClickListener?.let {
                        it(movie.id)
                    }
                }
                constraintLayout.setOnLongClickListener {
                    onItemLongClickListener?.let {
                        favoriteImageView.visibility = View.VISIBLE
                        movie.isFavorite = true
                        it(movie)
                    }
                    return@setOnLongClickListener true
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemBinding =
            MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(itemBinding)
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = differ.currentList[position]
        holder.bind(movie)
    }

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var onItemLongClickListener: ((Movie) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: (Movie) -> Unit) {
        onItemLongClickListener = listener
    }
}