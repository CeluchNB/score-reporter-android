package com.noah.scorereporter.di

import com.noah.scorereporter.pages.IPageRepository
import com.noah.scorereporter.pages.PageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class PagesModule {

    @Binds
    abstract fun bindPageRepository(repository: PageRepository): IPageRepository
}