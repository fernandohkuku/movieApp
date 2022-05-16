package com.fernando.movieapp.presentation.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.fernando.core.domain.entities.MovieEntity
import com.fernando.movieapp.databinding.ItemMovieBinding
import kotlin.math.roundToInt

class MovieViewHolder(
    private val binding: ItemMovieBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(parent: ViewGroup): MovieViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = ItemMovieBinding.inflate(inflater, parent, false)
            return MovieViewHolder(view)
        }
    }

    fun bindTo(movie: MovieEntity?) = with(binding) {
        ivMovie.setImageURI(movie?.poster?.toUri())
    }

}