package com.noah.scorereporter.pages.team

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TeamPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private lateinit var seasonFragment: SeasonListFragment
    private var followerFragment: FollowerListFragment = FollowerListFragment()

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                seasonFragment = SeasonListFragment()
                seasonFragment
            }
            1 -> {
                followerFragment = FollowerListFragment()
                followerFragment
            }
            else -> {
                FollowerListFragment()
            }
        }
    }
}