package com.pantheons.gamifiedfitness.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.pantheons.gamifiedfitness.data.remote.FirebaseAuthSource
import com.pantheons.gamifiedfitness.data.repository.AuthRepositoryImpl
import dagger.Provides


@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        firebaseAuthSource: FirebaseAuthSource
    ): AuthRepositoryImpl
}