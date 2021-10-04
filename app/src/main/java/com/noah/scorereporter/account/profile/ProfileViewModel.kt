package com.noah.scorereporter.account.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noah.scorereporter.account.IUserProfileRepository
import com.noah.scorereporter.model.UserProfile
import com.noah.scorereporter.network.Result
import com.noah.scorereporter.network.succeeded
import com.noah.scorereporter.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: IUserProfileRepository): ViewModel() {

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _userProfile: MutableLiveData<UserProfile?> = MutableLiveData(null)
    val userProfile: LiveData<UserProfile?>
        get() = _userProfile

    private val _getProfileError: MutableLiveData<Boolean> = MutableLiveData(false)
    val getProfileError: LiveData<Boolean>
        get() = _getProfileError

    private val _logoutSuccess: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))
    val logoutSuccess: LiveData<Event<Boolean>>
        get() = _logoutSuccess

    fun fetchUserProfile() {
        _loading.value = true
        viewModelScope.launch {
            repository.getProfile().let { result ->
                if (result.succeeded) {
                    _userProfile.value = (result as Result.Success).data
                } else {
                    _getProfileError.value = true
                    _userProfile.value = null
                }
            }
            _loading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            val result = repository.logout()
            if (result.succeeded) {
                _logoutSuccess.value = Event((result as Result.Success).data)
            } else {
                _logoutSuccess.value = Event(false)
            }
        }
    }

    fun hasSavedToken() = repository.hasSavedToken()

    fun setUserProfile(user: UserProfile) {
        _userProfile.value = user
    }
}