package com.noah.scorereporter.pages.season

import androidx.lifecycle.*
import com.noah.scorereporter.data.model.Game
import com.noah.scorereporter.data.model.Season
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.data.network.DispatcherProvider
import com.noah.scorereporter.pages.IPageRepository
import com.noah.scorereporter.pages.PageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SeasonViewModel @Inject constructor(
    private val repository: IPageRepository,
    private val dispatchers: DispatcherProvider
): ViewModel() {
    val id: MutableLiveData<String> = MutableLiveData()

    val team: MutableLiveData<Team> = MutableLiveData()

    private val _season: LiveData<Season> = id.switchMap {
        liveData(dispatchers.io()) {
            emitSource(repository.getSeasonById(it).asLiveData())
        }
    }

    val season: LiveData<Season>
        get() = _season

    val games: LiveData<List<Game>> = season.switchMap { s ->
        liveData(dispatchers.io()) {
            emitSource(repository.getGamesOfSeason(s.games.map { it.game }).asLiveData())
        }
    }
}