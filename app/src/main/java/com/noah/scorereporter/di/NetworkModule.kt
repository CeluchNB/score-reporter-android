package com.noah.scorereporter.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.noah.scorereporter.account.UserProfileDataSource
import com.noah.scorereporter.data.local.ReporterDatabase
import com.noah.scorereporter.data.network.PageService
import com.noah.scorereporter.data.network.UserDataSource
import com.noah.scorereporter.data.network.UserService
import com.noah.scorereporter.util.Constants
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

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

        @Provides
        fun providePageService(retrofit: Retrofit): PageService =
            retrofit.create(PageService::class.java)

        @Provides
        @Singleton
        fun provideReporterDatabase(@ApplicationContext context: Context) =
            Room.databaseBuilder(
                context,
                ReporterDatabase::class.java,
                "reporter-database.db"
            ).build()

        @Provides
        fun provideMasterKey(@ApplicationContext context: Context): MasterKey =
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

        @Provides
        fun provideEncryptedSharedPreferences(@ApplicationContext context: Context, masterKey: MasterKey): SharedPreferences =
            EncryptedSharedPreferences.create(
                context,
                Constants.SHARED_PREFS,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
    }

}