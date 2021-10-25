package com.noah.scorereporter.pages.season

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.noah.scorereporter.data.model.Game
import com.noah.scorereporter.databinding.ItemGameBinding

class GameListAdapter(val list: List<Game>) : RecyclerView.Adapter<GameListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGameBinding.inflate(LayoutInflater.from(parent.context), null, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: ItemGameBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(game: Game, position: Int) {
            Log.d("Noah", "Game: $game")
            binding.game = game
            binding.root.setOnClickListener {

            }
        }
    }
}