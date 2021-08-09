package com.noah.scorereporter.di

import com.noah.scorereporter.account.UserProfileDataSource
import com.noah.scorereporter.network.UserDataSource
import com.noah.scorereporter.network.UserService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    abstract fun bindUserDataSource(dataSource: UserProfileDataSource) : UserDataSource

    companion object {
        @Provides
        fun provideRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl("https://score-reporter.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        @Provides
        fun provideUserService(retrofit: Retrofit): UserService =
            retrofit.create(UserService::class.java)
    }

}