package com.fernando.core.presentation.ui.loader

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.fernando.core.databinding.RowStateLoadBinding

class LoadStateViewHolder(
    private val binding: RowStateLoadBinding,
    retry:() -> Unit
) : RecyclerView.ViewHolder(binding.root){

    init {
        binding.btnRetry.setOnClickListener { retry.invoke() }
    }

    companion object{
        fun create(parent: ViewGroup, retry: () -> Unit): LoadStateViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = RowStateLoadBinding.inflate(inflater, parent, false)
            return LoadStateViewHolder(view, retry)
        }
    }

    fun bindTo(loadState: LoadState) = with(binding){
        if (loadState is LoadState.Error){
            binding.btnRetry.text = loadState.error.message
        }
        binding.progressBar.isGone  = loadState !is LoadState.Loading
        binding.btnRetry.isGone = loadState is LoadState.Loading
        binding.btnRetry.isGone = loadState !is LoadState.Error
    }


}