package com.noah.scorereporter.pages.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.noah.scorereporter.R
import com.noah.scorereporter.data.model.Season

class SeasonListFragment : Fragment() {

    private var seasonAdapter: SeasonListAdapter? = null
    private lateinit var seasonList: RecyclerView

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
    }

    fun updateSeasonList(seasons: List<Season>) {
        val dateList = seasons.map {
            it.startDate
        }

        if (seasonAdapter == null) {
            seasonAdapter = SeasonListAdapter(dateList)
        } else {
            seasonAdapter?.list = dateList
        }

        seasonList.adapter = seasonAdapter
    }
}