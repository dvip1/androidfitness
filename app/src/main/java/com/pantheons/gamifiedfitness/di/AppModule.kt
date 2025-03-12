package com.pantheons.gamifiedfitness.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.pantheons.gamifiedfitness.data.remote.FirebaseAuthSource
import com.pantheons.gamifiedfitness.data.repository.AuthRepositoryImpl
import com.pantheons.gamifiedfitness.util.healthconnect.HealthConnectManager
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped


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

@Module
@InstallIn(ViewModelComponent::class)
object HealthConnectModule {

    @Provides
    @ViewModelScoped
    fun provideHealthConnectManager(@ApplicationContext context: Context): HealthConnectManager {
        return HealthConnectManager(context)
    }
}
