package com.imax.edumeet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imax.edumeet.data.MainRepository
import com.imax.edumeet.data.models.Notifications
import com.imax.edumeet.data.models.Register
import com.imax.edumeet.data.models.RegisterResponse
import com.imax.edumeet.data.models.Student
import com.imax.edumeet.models.Group
import com.imax.edumeet.models.Login
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

    private val _getStudent = MutableSharedFlow<Result<Student>>()
    val getStudent: Flow<Result<Student>>
        get() = _getStudent

    private val _getGroups = MutableSharedFlow<Result<List<Group>>>()
    val getGroups: Flow<Result<List<Group>>>
        get() = _getGroups

    private val _getCountOfNotification = MutableSharedFlow<Result<Int>>()
    val getCountOfNotification: Flow<Result<Int>>
        get() = _getCountOfNotification

    fun register(data: Register) {
        viewModelScope.launch {
            _authResult.emit(repository.register(data))
        }
    }

    fun login(data: Login) {
        viewModelScope.launch {
            _authResult.emit(repository.login(data))
        }
    }

    fun editPhoneAndPassword(data: Login) {
        viewModelScope.launch {
            _authResult.emit(repository.editPhoneAndPassword(data))
        }
    }

    fun editName(data: Group) {
        viewModelScope.launch {
            _authResult.emit(repository.editName(data))
        }
    }

    fun getNotifications(studentId: String) {
        viewModelScope.launch {
            showNotification(studentId)
            _getNotificationResult.emit(repository.getNotifications(studentId))
        }
    }

    fun getCountOfNotification(studentId: String) {
        viewModelScope.launch {
            _getCountOfNotification.emit(repository.getCountOfNotification(studentId))
        }
    }

    fun showNotification(studentId: String) {
        viewModelScope.launch {
            repository.showNotification(studentId)
        }
    }

    fun getStudent() {
        viewModelScope.launch {
            _getStudent.emit(repository.getStudent())
        }
    }

    fun getGroups() {
        viewModelScope.launch {
            _getGroups.emit(repository.getGroups())
        }
    }

}