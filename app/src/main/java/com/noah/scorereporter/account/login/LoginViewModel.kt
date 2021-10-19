package com.noah.scorereporter.account.login

import androidx.lifecycle.*
import com.noah.scorereporter.account.IUserProfileRepository
import com.noah.scorereporter.data.model.UserProfile
import com.noah.scorereporter.data.network.Result
import com.noah.scorereporter.data.network.UserNetworkError
import com.noah.scorereporter.data.network.succeeded
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

        viewModelScope.launch {
            try {
                _loading.value = true
                val result = repository.login(email, password)
                _userProfile.value = result
            } catch (exception: UserNetworkError) {
                _loginError.value = exception.message
            } finally {
                _loading.value = false
            }
        }
    }
}