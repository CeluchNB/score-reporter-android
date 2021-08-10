package com.noah.scorereporter.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.noah.scorereporter.account.UserProfileDataSource
import com.noah.scorereporter.network.UserDataSource
import com.noah.scorereporter.network.UserService
import com.noah.scorereporter.util.Constants
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

        @Provides
        fun provideMasterKey(@ApplicationContext context: Context): MasterKey = MasterKey.Builder(context)
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