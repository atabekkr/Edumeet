package com.imax.edumeet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imax.edumeet.data.MainRepository
import com.imax.edumeet.data.models.Notifications
import com.imax.edumeet.data.models.Register
import com.imax.edumeet.data.models.RegisterResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _authResult = MutableSharedFlow<Result<RegisterResponse>>()
    val authResult: Flow<Result<RegisterResponse>>
        get() = _authResult

    private val _getNotificationResult = MutableSharedFlow<Result<Notifications>>()
    val getNotificationResult: Flow<Result<Notifications>>
        get() = _getNotificationResult

    fun register(data: Register) {
        viewModelScope.launch {
            _authResult.emit(repository.register(data))
        }
    }

    fun getNotifications(studentId: String) {
        viewModelScope.launch {
            _getNotificationResult.emit(repository.getNotifications(studentId))
        }
    }

}