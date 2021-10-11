package com.noah.scorereporter.pages.team

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.noah.scorereporter.R
import com.noah.scorereporter.data.model.Follower

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

        val bundle = arguments
        if (bundle != null) {
            if (bundle.containsKey("SEASONS")) {
                Log.d("Noah", "got seasons")
                val seasons = bundle.getStringArrayList("SEASONS")
                Log.d("Noah", seasons?.size.toString())
            }
        }
    }
}