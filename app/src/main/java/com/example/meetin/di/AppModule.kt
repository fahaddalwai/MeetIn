package com.example.meetin.di

import com.example.meetin.domain.repository.Repository
import com.example.meetin.remote.RepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Provides
    @Singleton
    fun provideAppApiRepository(
       auth:FirebaseAuth,
       db:Firebase
    ): Repository {
        return RepositoryImpl(auth,db)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Singleton
    @Provides
    fun provideFirestoreDatabase() = Firebase

}