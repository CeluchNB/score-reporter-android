package com.noah.scorereporter.fake

import com.noah.scorereporter.data.network.PageService
import com.noah.scorereporter.data.network.UserService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MockPageClient {
    fun createValid() : PageService {
        return Retrofit.Builder()
            .client(OkHttpClient.Builder().addInterceptor(MockUserInterceptor(true)).build())
            .baseUrl("https://score-reporter.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(PageService::class.java)
    }

    fun createInvalid(): PageService {
        return Retrofit.Builder()
            .client(OkHttpClient.Builder().addInterceptor(MockUserInterceptor(false)).build())
            .baseUrl("https://score-reporter.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(PageService::class.java)
    }
}