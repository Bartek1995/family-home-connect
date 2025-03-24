package com.familyhomeconnect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.familyhomeconnect.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val currentUser: LiveData<FirebaseUser?> = authRepository.currentUser

    fun signIn(onSuccess: (String?) -> Unit, onError: (String) -> Unit) {
        authRepository.signIn(onSuccess, onError)
    }

    fun signOut() {
        authRepository.signOut()
    }
}
