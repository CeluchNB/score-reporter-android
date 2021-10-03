package com.noah.scorereporter.account.login

import androidx.lifecycle.*
import com.noah.scorereporter.account.IUserProfileRepository
import com.noah.scorereporter.model.UserProfile
import com.noah.scorereporter.network.Result
import com.noah.scorereporter.network.succeeded
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: IUserProfileRepository) : ViewModel() {

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _userProfile: MutableLiveData<UserProfile?> = MutableLiveData(null)
    val userProfile: LiveData<UserProfile?>
        get() = _userProfile

    private val _loginError: MutableLiveData<String> = MutableLiveData(null)
    val loginError: LiveData<String>
        get() = _loginError


    fun login(email: String, password: String) {
        _loading.value = true
        viewModelScope.launch {
            repository.login(email, password).let { result ->
                if (result.succeeded) {
                    _userProfile.value = (result as Result.Success).data
                } else {
                    _loginError.value = (result as Result.Error).exception.message
                }
            }
            _loading.value = false
        }
    }
}