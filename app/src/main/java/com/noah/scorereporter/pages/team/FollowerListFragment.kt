package com.noah.scorereporter.pages.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.noah.scorereporter.R
import com.noah.scorereporter.data.model.TeamFollower
import com.noah.scorereporter.data.model.Role

class FollowerListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follower_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val followerList = view.findViewById<RecyclerView>(R.id.list_follower)
        followerList.layoutManager = LinearLayoutManager(requireContext())
        followerList.addItemDecoration(DividerItemDecoration(requireContext(), OrientationHelper.VERTICAL))

        val bundle = arguments
        if (bundle != null) {
            if (bundle.containsKey("FOLLOWERS")) {
                // bundle.getParcelableArrayList<Follower>("FOLLOWERS")
                val followers = listOf(
                    TeamFollower("Noah Celuch", Role.FAN),
                    TeamFollower("Amy Celuch", Role.COACH),
                    TeamFollower("Peyton Celuch", Role.PLAYER),
                    TeamFollower("Evan Celuch", Role.PLAYER),
                    TeamFollower("Henry Smith", Role.PLAYER),
                    TeamFollower("Simon Smith", Role.PLAYER),
                    TeamFollower("Arthur Smith", Role.PLAYER)
                )
                followers?.let {
                    val followerAdapter = FollowerListAdapter(it)
                    followerList.adapter = followerAdapter
                }
            }
        }
    }
}