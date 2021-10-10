package com.noah.scorereporter.pages.team

import androidx.lifecycle.*
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.data.network.Result
import com.noah.scorereporter.data.network.succeeded
import com.noah.scorereporter.pages.IPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(private val repository: IPageRepository): ViewModel() {

    val id = MutableLiveData<String>()

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _team: MutableLiveData<Team> = id.switchMap {
            liveData(Dispatchers.Default) {
                val result = repository.getTeamById(it)
                if (result.succeeded) {
                    result as Result.Success
                    emit(result.data)
                }
            }
        } as MutableLiveData<Team>

    val team: LiveData<Team>
        get() = _team

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

                val result = repository.followTeam(it)
                if (result.succeeded) {
                    result as Result.Success
                    _team.value = result.data
                    _followSuccess.value = true
                } else {
                    _followSuccess.value = false
                }
                _loading.value = false
            }
        }
    }
}