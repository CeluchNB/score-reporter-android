package com.noah.scorereporter.pages.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.noah.scorereporter.R

class FollowerListFragment : Fragment() {

    private lateinit var followerList: RecyclerView
    private var followerAdapter: FollowerListAdapter? = null
    private val viewModel: TeamViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follower_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followerList = view.findViewById(R.id.list_follower)
        followerList.layoutManager = LinearLayoutManager(requireContext())
        followerList.addItemDecoration(DividerItemDecoration(requireContext(), OrientationHelper.VERTICAL))

        viewModel.followers.observe(viewLifecycleOwner) {
            // TODO implement diff utils
            if (followerAdapter == null) {
                followerAdapter = FollowerListAdapter(it)
                followerList.adapter = followerAdapter
            } else {
                followerAdapter?.list = it
            }
            followerAdapter?.notifyDataSetChanged()
        }
    }
}