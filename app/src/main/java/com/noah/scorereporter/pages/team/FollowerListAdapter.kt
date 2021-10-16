package com.noah.scorereporter.pages.team

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.noah.scorereporter.data.model.TeamFollower
import com.noah.scorereporter.databinding.ItemTeamBinding
import com.noah.scorereporter.pages.model.Follower

class FollowerListAdapter(var list: List<Follower>) : RecyclerView.Adapter<FollowerListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTeamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val binding: ItemTeamBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(follower: Follower) {
            binding.follower = follower
        }
    }
}