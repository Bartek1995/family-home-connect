package com.familyhomeconnect.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class GoogleAuthUiClient @Inject constructor(
    private val context: Context,
    private val credentialManager: CredentialManager,
    private val webClientId: String,
    private val firebaseAuth: FirebaseAuth
) {

    fun getCurrentUser() = firebaseAuth.currentUser

    fun isSignedIn(): Boolean = getCurrentUser() != null

    fun getSignedInUserEmail(): String? = getCurrentUser()?.email

    fun signIn(
        onSuccess: (String?) -> Unit,
        onError: (String) -> Unit
    ) {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(webClientId)
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = credentialManager.getCredential(context, request)
                val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
                firebaseAuthWithGoogle(credential.idToken, onSuccess, onError)
            } catch (e: GetCredentialException) {
                Log.e("GoogleAuthUiClient", "Credential error: ${e.localizedMessage}")
                onError("Credential error: ${e.localizedMessage}")
            }
        }
    }

    private fun firebaseAuthWithGoogle(
        idToken: String,
        onSuccess: (String?) -> Unit,
        onError: (String) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(getCurrentUser()?.email)
                } else {
                    Log.w("GoogleAuthUiClient", "Firebase auth failed", task.exception)
                    onError("Firebase auth failed: ${task.exception?.localizedMessage}")
                }
            }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}
