package com.noah.scorereporter.pages.team

import androidx.lifecycle.*
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.pages.IPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(private val repository: IPageRepository): ViewModel() {

    val id = MutableLiveData<String>()

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    val team: MutableLiveData<Team>
        get() = id.switchMap {
            liveData {
                _loading.value = true
                repository.getTeamById(it).collect {
                    emit(it)
                    _loading.value = false
                }
            }
        } as MutableLiveData<Team>

    private val _followSuccess = MutableLiveData(false)
    val followSuccess: LiveData<Boolean>
        get() = _followSuccess

    fun follow() {
        _loading.value = true
        viewModelScope.launch {
            id.value?.let {
                if (it.isEmpty()) {
                    _followSuccess.value = false
                    _loading.value = false
                    return@launch
                }

                repository.followTeam(it).take(1).collect {
                    team.value = it
                    _followSuccess.value = true
                    _loading.value = false
                }
            }
            _loading.value = false
        }
    }
}