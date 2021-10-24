package com.noah.scorereporter.pages.team

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.noah.scorereporter.util.ListOnClickListener

class TeamPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private lateinit var seasonFragment: SeasonListFragment
    private lateinit var followerFragment: FollowerListFragment
    var seasonListener: ListOnClickListener? = null

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                seasonFragment = SeasonListFragment()
                seasonFragment.listener = seasonListener
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