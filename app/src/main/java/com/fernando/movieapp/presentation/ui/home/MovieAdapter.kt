package com.fernando.movieapp.presentation.ui.home

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fernando.core.domain.entities.MovieEntity
import com.fernando.ui_ktx.widget.asyncListDiffer

class MovieAdapter : PagingDataAdapter<MovieEntity, RecyclerView.ViewHolder>(
    asyncListDiffer(
        areContentsTheSame = { old, new -> old.id == new.id },
        areItemsTheSame = { old, new -> old.id == new.id }
    )
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MovieViewHolder.create(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieViewHolder) {
            val movie = getItem(position)
            holder.bindTo(movie)
        }
    }

    fun getMovie(position: Int) = try {
        getItem(position)
    } catch (e: IndexOutOfBoundsException) {
        null
    }
}