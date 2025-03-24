package com.familyhomeconnect.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.familyhomeconnect.auth.GoogleAuthUiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val googleAuthUiClient: GoogleAuthUiClient
) {
    private val _currentUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser: LiveData<FirebaseUser?> get() = _currentUser

    init {
        firebaseAuth.addAuthStateListener { auth ->
            _currentUser.postValue(auth.currentUser)
        }
    }

    fun signIn(onSuccess: (String?) -> Unit, onError: (String) -> Unit) {
        googleAuthUiClient.signIn(
            onSuccess = { email ->
                onSuccess(email)
            },
            onError = { error ->
                onError(error)
            }
        )
    }

    fun signOut() {
        googleAuthUiClient.signOut()
    }
}
