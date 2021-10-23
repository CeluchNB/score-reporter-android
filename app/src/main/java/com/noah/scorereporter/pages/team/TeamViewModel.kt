package com.noah.scorereporter.pages.team

import androidx.lifecycle.*
import com.noah.scorereporter.data.model.Season
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.data.network.DispatcherProvider
import com.noah.scorereporter.data.network.PageNetworkError
import com.noah.scorereporter.pages.IPageRepository
import com.noah.scorereporter.pages.model.Follower
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val repository: IPageRepository,
    private val dispatchers: DispatcherProvider
): ViewModel() {

    val id = MutableLiveData<String>()

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _team: MutableLiveData<Team> = id.switchMap {
        _loading.value = true
            liveData(dispatchers.io()) {
                emitSource(repository.getTeamById(it).asLiveData())
                _loading.postValue(false)
            }
        } as MutableLiveData<Team>

    val team: LiveData<Team>
        get() = _team

    private val _followSuccess = MutableLiveData(false)
    val followSuccess: LiveData<Boolean>
        get() = _followSuccess

    private val _seasons = _team.switchMap {
        liveData(dispatchers.io()) {
            emitSource(repository.getSeasonsOfTeam(it.seasons.map { it.season }).asLiveData())
        }
    }
    val seasons: LiveData<List<Season>>
        get() = _seasons

    private val _followers = _team.switchMap {
        liveData(dispatchers.io()) {
            emitSource(repository.getFollowersOfTeam(it.followers).asLiveData())
        }
    }
    val followers: LiveData<List<Follower>>
        get() = _followers

    val canFollow: LiveData<Boolean> = team.switchMap {
        liveData(dispatchers.io()) {
            emit(repository.canFollow(it.id))
        }
    }

    fun follow() {
        _loading.value = true
        id.value?.let {
            if (it.isEmpty()) {
                _followSuccess.value = false
                _loading.value = false
                return
            }
            viewModelScope.launch {
                try {
                    repository.followTeam(it).collect { team ->
                        _followSuccess.value = true
                        _team.value = team
                    }
                } catch (exception: PageNetworkError) {
                    _followSuccess.value = false
                }
            }
        }
        _loading.value = false
    }
}