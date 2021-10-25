package com.noah.scorereporter.account.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noah.scorereporter.account.IUserProfileRepository
import com.noah.scorereporter.data.model.UserProfile
import com.noah.scorereporter.data.network.Result
import com.noah.scorereporter.data.network.UserNetworkError
import com.noah.scorereporter.data.network.succeeded
import com.noah.scorereporter.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: IUserProfileRepository): ViewModel() {

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _logoutLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val logoutLoading: LiveData<Boolean>
        get() = _logoutLoading

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
        viewModelScope.launch {
            try {
                _loading.value = true
                val result = repository.getProfile()
                _userProfile.value = result
            } catch (exception: UserNetworkError) {
                _getProfileError.value = true
                _userProfile.value = null
            } finally {
                _loading.value = false
            }
        }
    }

    fun logout() {

        viewModelScope.launch {
            try {
                _logoutLoading.value = true
                val result = repository.logout()
                _logoutSuccess.value = Event(true)
            } catch (exception: UserNetworkError) {
                _logoutSuccess.value = Event(false)
            } finally {
                _logoutLoading.value = false
            }
        }
    }

    fun hasSavedToken() = repository.hasSavedToken()

    fun setUserProfile(user: UserProfile) {
        _userProfile.value = user
    }
}