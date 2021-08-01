package com.noah.scorereporter.account.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noah.scorereporter.account.AccountRepository
import com.noah.scorereporter.model.UserProfile
import com.noah.scorereporter.network.Result
import com.noah.scorereporter.network.succeeded
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AccountRepository) : ViewModel() {

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _userProfile: MutableLiveData<UserProfile> = MutableLiveData(null)
    val userProfile: LiveData<UserProfile>
        get() = _userProfile

    private val _loginError: MutableLiveData<String> = MutableLiveData(null)
    val loginError: LiveData<String>
        get() = _loginError


    fun onLoginClicked(email: String, password: String) {
        _loading.value = true
        viewModelScope.launch {
            repository.login(email, password).let { result ->
                if (result.succeeded) {
                    _userProfile.value = (result as Result.Success).data
                } else {
                    _loginError.value = (result as Result.Error).exception.message
                }
            }
        }
        _loading.value = false
    }
}