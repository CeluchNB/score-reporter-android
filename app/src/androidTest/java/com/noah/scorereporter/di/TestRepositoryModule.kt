package com.noah.scorereporter.di

import com.noah.scorereporter.account.IUserProfileRepository
import com.noah.scorereporter.fake.AndroidFakeUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.testing.TestInstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AccountModule::class]
)
abstract class TestRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindRepository(repo: AndroidFakeUserRepository): IUserProfileRepository
}