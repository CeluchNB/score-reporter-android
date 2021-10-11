package com.noah.scorereporter.pages.team

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.noah.scorereporter.data.model.Follower
import com.noah.scorereporter.data.model.Team
import java.util.ArrayList

class TeamPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, val team: Team) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = SeasonListFragment()

                val bundle = Bundle()
                bundle.putStringArrayList("SEASONS", team.seasons as ArrayList<String>)
                fragment.arguments = bundle

                fragment
            }
            1 -> {
                val fragment = FollowerListFragment()

                val bundle = Bundle()
                bundle.putParcelableArrayList("FOLLOWERS", team.followers as ArrayList<Follower>)
                fragment.arguments = bundle

                fragment
            }
            else -> {
                val fragment = FollowerListFragment()
                fragment
            }
        }
    }

}