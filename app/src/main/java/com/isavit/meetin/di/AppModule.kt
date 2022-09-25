package com.isavit.meetin.di

import android.app.Application
import com.isavit.meetin.domain.repository.Repository
import com.isavit.meetin.domain.userPrefs.UserPreference
import com.isavit.meetin.domain.userPrefs.UserPreferenceImpl
import com.isavit.meetin.remote.RepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppApiRepository(
        auth: FirebaseAuth,
        db: Firebase,
        storage: FirebaseStorage
    ): Repository {
        return RepositoryImpl(auth, db, storage)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Singleton
    @Provides
    fun provideFirestoreDatabase() = Firebase

    @Singleton
    @Provides
    fun provideFirebaseStorage() = FirebaseStorage.getInstance()

    @Singleton
    @Provides
    fun providesUserPref(app: Application): UserPreference =
        UserPreferenceImpl(app)


}