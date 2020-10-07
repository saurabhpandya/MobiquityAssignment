package com.mobiquityassignment.ui.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobiquityassignment.R
import com.mobiquityassignment.databinding.RawFavoriteBinding
import com.mobiquityassignment.utility.OnItemClickListner

class FavoriteAdapter(
    var arylstFavorite: List<String>
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    lateinit var onItemClickListner: OnItemClickListner

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        val binding: RawFavoriteBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.raw_favorite,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arylstFavorite.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(arylstFavorite.get(position))
        holder.itemView.setOnClickListener {
            onItemClickListner.onItemClickListner(position)
        }
        holder.binding.imgvwDelete.setOnClickListener {
            onItemClickListner.onDeleteClickListner(position)
        }
    }

    fun setChat(arylstFavorite: List<String>) {
        this.arylstFavorite = arylstFavorite
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: RawFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(favorite: String) {
            binding.txtvwFavorite.text = favorite
        }
    }

}
