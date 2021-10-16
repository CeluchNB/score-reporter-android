package com.noah.scorereporter.pages.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.noah.scorereporter.R

class SeasonListFragment : Fragment() {

    private var seasonAdapter: SeasonListAdapter? = null
    private lateinit var seasonList: RecyclerView
    private val viewModel: TeamViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_season_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seasonList = view.findViewById(R.id.list_season)
        seasonList.layoutManager = GridLayoutManager(requireContext(), 4)
        seasonList.addItemDecoration(SeasonItemDecoration())

        viewModel.seasons.observe(viewLifecycleOwner) { seasons ->
            // TODO implement diff utils
            if (seasonAdapter == null) {
                seasonAdapter = SeasonListAdapter(seasons.map { it.startDate })
                seasonList.adapter = seasonAdapter
            } else {
                seasonAdapter?.list = seasons.map { it.startDate }
            }

            seasonAdapter?.notifyDataSetChanged()
        }
    }
}