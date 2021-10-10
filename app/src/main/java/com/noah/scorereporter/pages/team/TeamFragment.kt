package com.noah.scorereporter.pages.team

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.noah.scorereporter.databinding.FragmentTeamBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class TeamFragment : Fragment() {

    private lateinit var binding: FragmentTeamBinding

    private val viewModel: TeamViewModel by viewModels()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        if (bundle != null) {
            val args = TeamFragmentArgs.fromBundle(bundle)
            viewModel.id.value = args.teamId
        }

        viewModel.team.observe(viewLifecycleOwner) {
            if (it != null) {
                val simpleDateFormat = SimpleDateFormat("yyyy", Locale.US)
                val date = simpleDateFormat.format(it.founded)
                Log.d("Noah", date)
            }
        }
    }
}