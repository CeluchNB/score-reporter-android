package com.noah.scorereporter.pages.team

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.noah.scorereporter.databinding.ItemSeasonBinding
import com.noah.scorereporter.util.ListOnClickListener
import java.util.*

class SeasonListAdapter(var list: List<Date>, val listener: ListOnClickListener?) : RecyclerView.Adapter<SeasonListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSeasonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: ItemSeasonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(year: Date, position: Int) {
            binding.date = year
            binding.buttonYear.setOnClickListener {
                listener?.onClick(position)
            }
        }
    }
}