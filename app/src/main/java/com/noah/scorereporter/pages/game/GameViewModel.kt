package com.noah.scorereporter.pages.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.noah.scorereporter.data.model.Game
import com.noah.scorereporter.pages.IPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(val repository: IPageRepository) : ViewModel() {

    val id: LiveData<String> = MutableLiveData()

    val _game: LiveData<Game> =
        liveData {

        }
}