package com.noah.scorereporter.pages.team

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.noah.scorereporter.R
import com.noah.scorereporter.data.model.Follower

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

        val bundle = arguments
        if (bundle != null) {
            if (bundle.containsKey("FOLLOWERS")) {
                Log.d("Noah", "got followers")
                val followers = bundle.getParcelableArrayList<Follower>("FOLLOWERS")
                Log.d("Noah", followers?.size.toString())
            }
        }
    }
}