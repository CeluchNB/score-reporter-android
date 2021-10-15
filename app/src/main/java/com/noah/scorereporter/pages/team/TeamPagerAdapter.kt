package com.noah.scorereporter.pages.team

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.noah.scorereporter.data.model.Season

class TeamPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var seasonFragment: SeasonListFragment? = null
    private var followerFragment: FollowerListFragment? = null

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                seasonFragment = SeasonListFragment()
                seasonFragment ?: SeasonListFragment()
            }
            1 -> {
                followerFragment = FollowerListFragment()
                followerFragment ?: FollowerListFragment()
            }
            else -> {
                FollowerListFragment()
            }
        }
    }

    fun updateSeasonList(seasons: List<Season>) {
        seasonFragment?.updateSeasonList(seasons)
    }

}