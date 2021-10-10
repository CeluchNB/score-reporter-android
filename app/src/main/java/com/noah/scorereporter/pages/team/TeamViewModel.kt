package com.noah.scorereporter.pages.team

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noah.scorereporter.data.model.Team
import com.noah.scorereporter.pages.IPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(private val repository: IPageRepository): ViewModel() {

    private val _loading: LiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _team: LiveData<Team?> = MutableLiveData(null)
    val team: LiveData<Team?>
        get() = _team

    private val _followSuccess: LiveData<Boolean> = MutableLiveData(false)
    val followSuccess: LiveData<Boolean>
        get() = _followSuccess

    fun follow() {

    }

    fun fetchTeam(id: String) {

    }
}