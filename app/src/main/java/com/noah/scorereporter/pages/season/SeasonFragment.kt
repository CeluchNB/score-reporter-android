package com.noah.scorereporter.pages.season

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.noah.scorereporter.databinding.FragmentSeasonBinding
import com.noah.scorereporter.pages.team.SeasonItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeasonFragment : Fragment() {

    private lateinit var binding: FragmentSeasonBinding
    private lateinit var adapter: GameListAdapter
    val viewModel: SeasonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeasonBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        if (bundle != null) {
            val args = SeasonFragmentArgs.fromBundle(bundle)
            viewModel.id.value = args.seasonId
            viewModel.team.value = args.team
        }

        binding.listGames.layoutManager = LinearLayoutManager(context)
        binding.listGames.addItemDecoration(DividerItemDecoration(requireContext(), OrientationHelper.VERTICAL))

        viewModel.season.observe(viewLifecycleOwner) {
            Log.d("Noah", "$it")
        }

        viewModel.gameList.observe(viewLifecycleOwner) {
            Log.d("Noah", "games: $it")
            adapter = GameListAdapter(it)
            binding.listGames.adapter = adapter
        }
    }
}