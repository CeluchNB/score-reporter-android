package com.noah.scorereporter.pages.team

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.databinding.FragmentTeamBinding
import com.noah.scorereporter.util.ListOnClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeamFragment : Fragment() {

    private lateinit var binding: FragmentTeamBinding
    private var pagerAdapter: TeamPagerAdapter? = null

    private val viewModel: TeamViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeamBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        if (bundle != null) {
            val args = TeamFragmentArgs.fromBundle(bundle)
            viewModel.id.value = args.teamId
        }

        viewModel.team.observe(viewLifecycleOwner) {
            if (pagerAdapter == null) {
                pagerAdapter = TeamPagerAdapter(childFragmentManager, lifecycle)
                pagerAdapter?.seasonListener = object : ListOnClickListener {
                    override fun onClick(position: Int) {
                        var seasonId: String? = null
                        var team: Team? = null
                        viewModel.seasons.value?.let { season ->
                            seasonId = season[position].id
                        }

                        viewModel.team.value?.let { t ->
                            team = t
                        }
                        if (seasonId != null && team != null) {
                            val action =
                                TeamFragmentDirections.actionTeamFragmentToSeasonFragment(seasonId, team)
                            findNavController().navigate(action)
                        }
                    }
                }
                binding.pagerFollowerSeason.adapter = pagerAdapter

                TabLayoutMediator(binding.pagerTabLayout, binding.pagerFollowerSeason) { tab, position ->
                    if (position == 0) {
                        tab.text = "Seasons"
                    } else {
                        tab.text = "Followers"
                    }
                }.attach()
            } else {
                pagerAdapter?.notifyDataSetChanged()
            }
        }
    }
}