package com.noah.scorereporter.pages.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.noah.scorereporter.R

class SeasonListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_season_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val seasonList = view.findViewById<RecyclerView>(R.id.list_season)
        seasonList.layoutManager = GridLayoutManager(requireContext(), 4)
        seasonList.addItemDecoration(SeasonItemDecoration())

        val bundle = arguments
        if (bundle != null) {
            if (bundle.containsKey("SEASONS")) {
                val seasons = listOf("2024", "2015", "2016", "2017", "2018", "2019", "2020", "2021") // bundle.getStringArrayList("SEASONS")

                seasons?.let {
                    val seasonAdapter = SeasonListAdapter(seasons)
                    seasonList.adapter = seasonAdapter
                }
            }
        }

    }
}