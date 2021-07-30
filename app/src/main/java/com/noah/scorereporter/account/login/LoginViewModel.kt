package com.noah.scorereporter.account.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noah.scorereporter.account.UserRepository
import com.noah.scorereporter.model.UserProfile
import com.noah.scorereporter.network.Result

class LoginViewModel(val repository: UserRepository) : ViewModel() {

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _userProfile: MutableLiveData<Result<UserProfile>> = MutableLiveData(null)
    val userProfile: LiveData<Result<UserProfile>>
        get() = _userProfile


    fun onLoginClicked(email: String, password: String) {

    }
}