package com.noah.scorereporter.pages.team

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.noah.scorereporter.databinding.ItemSeasonBinding

class SeasonListAdapter(private val list: List<String>) : RecyclerView.Adapter<SeasonListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSeasonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: ItemSeasonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(year: String) {
            binding.year = year
        }
    }
}