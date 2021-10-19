package com.noah.scorereporter.account.signup

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
class SignUpViewModel @Inject constructor(private val repository: IUserProfileRepository) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get () = _loading

    private val _user: MutableLiveData<Event<UserProfile>> = MutableLiveData(null)
    val user: LiveData<Event<UserProfile>>
        get () = _user

    private val _error: MutableLiveData<Event<String>> = MutableLiveData(null)
    val error: LiveData<Event<String>>
        get () = _error

    fun signup(firstName: String, lastName: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val result = repository.signUp(firstName, lastName, email, password)
                _user.value = Event(result)
            } catch (exception: UserNetworkError) {
                _error.value = Event(
                    exception.message ?: "Unable to sign up"
                )
            } finally {
                _loading.value = false
            }
        }
    }

}