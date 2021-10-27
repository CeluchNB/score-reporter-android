package com.noah.scorereporter.pages.game

import androidx.lifecycle.*
import com.noah.scorereporter.data.model.GameItem
import com.noah.scorereporter.data.network.DispatcherProvider
import com.noah.scorereporter.pages.IPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: IPageRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    val id: MutableLiveData<String> = MutableLiveData()

    private val _game: LiveData<GameItem> = id.switchMap {
        liveData(dispatchers.io()) {
            emitSource(repository.getGame(it).asLiveData())
        }
    }
    val game: LiveData<GameItem>
        get() = _game
}