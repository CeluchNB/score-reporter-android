package com.noah.scorereporter.pages.team

import androidx.lifecycle.*
import com.noah.scorereporter.data.model.Season
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.pages.IPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(private val repository: IPageRepository): ViewModel() {

    val id = MutableLiveData<String>()

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _team: MutableLiveData<Team> = id.switchMap {
        _loading.value = true
            liveData(Dispatchers.IO) {
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
        liveData(Dispatchers.IO) {
            emitSource(repository.getSeasonsOfTeam(it.seasons.map { it.season }).asLiveData())
        }
    }
    val seasons: LiveData<List<Season>>
        get() = _seasons

    fun follow() {
        _loading.value = true
        viewModelScope.launch {
            id.value?.let {
                if (it.isEmpty()) {
                    _followSuccess.value = false
                    _loading.value = false
                    return@launch
                }

                repository.followTeam(it).collect {
                    _followSuccess.value = true
                    _team.value = it
                }
            }
            _loading.value = false
        }
    }
}