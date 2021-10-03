package com.noah.scorereporter.account.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noah.scorereporter.account.IUserProfileRepository
import com.noah.scorereporter.model.UserProfile
import com.noah.scorereporter.network.Result
import com.noah.scorereporter.network.succeeded
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: IUserProfileRepository): ViewModel() {

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _user: MutableLiveData<UserProfile?> = MutableLiveData(null)
    val user: LiveData<UserProfile?>
        get() = _user

    private val _getProfileError: MutableLiveData<Boolean> = MutableLiveData(false)
    val getProfileError: LiveData<Boolean>
        get() = _getProfileError

    fun getUserProfile() {
        _loading.value = true
        viewModelScope.launch {
            repository.getProfile().let { result ->
                if (result.succeeded) {
                    _user.value = (result as Result.Success).data
                } else {
                    _getProfileError.value = true
                    _user.value = null
                }
            }
            _loading.value = false
        }
    }
}