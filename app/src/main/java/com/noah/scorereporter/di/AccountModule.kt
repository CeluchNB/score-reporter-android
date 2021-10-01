package com.noah.scorereporter.di

import com.noah.scorereporter.account.IUserProfileRepository
import com.noah.scorereporter.account.UserProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AccountModule {

    @Binds
    abstract fun bindUserRepository(repository: UserProfileRepository) : IUserProfileRepository
}