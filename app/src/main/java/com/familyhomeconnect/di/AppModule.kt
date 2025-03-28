package com.familyhomeconnect.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.familyhomeconnect.auth.GoogleAuthUiClient
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager =
        CredentialManager.create(context)

    @Provides
    @Singleton
    fun provideGoogleAuthUiClient(
        @ApplicationContext context: Context,
        credentialManager: CredentialManager,
        firebaseAuth: FirebaseAuth
    ): GoogleAuthUiClient = GoogleAuthUiClient(
        context = context,
        credentialManager = credentialManager,
        webClientId = context.getString(com.familyhomeconnect.R.string.default_web_client_id),
        firebaseAuth = firebaseAuth
    )
}
