package com.coriolang.lighteducation.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.coriolang.lighteducation.LeducationApp
import com.coriolang.lighteducation.data.Authentication
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authentication: Authentication
) : ViewModel() {

    val signedIn: StateFlow<Boolean>
        get() = authentication.signedIn

    val exception: StateFlow<String>
        get() = authentication.exception

    private val handler = CoroutineExceptionHandler { _, throwable ->
        setException(throwable.message!!)
    }

    private fun setException(message: String) {
        authentication.updateException(message)
    }

    fun createUser(displayName: String, email: String, password: String) {
        viewModelScope.launch(handler) {
            authentication.createUser(displayName, email, password)
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch(handler) {
            authentication.signIn(email, password)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authentication.signOut()
        }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val firebaseAuth = (this[APPLICATION_KEY] as LeducationApp).authentication

                AuthViewModel(
                    authentication = firebaseAuth
                )
            }
        }
    }
}