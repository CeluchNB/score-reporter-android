package com.noah.scorereporter.fake

import com.google.gson.Gson
import com.noah.scorereporter.TestConstants
import okhttp3.*

class MockPageInterceptor(private val valid: Boolean) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val gson = Gson()
        val uri = chain.request().url().uri().toString()
        var response = ""

        when {
            uri.contains(Regex("/team/.")) -> {
                response = if (valid) {
                    gson.toJson(
                        TestConstants.TEAM_RESPONSE
                    )
                } else {
                    TestConstants.TEAM_ERROR
                }
            }
        }


        if (valid) {
            return chain.proceed(chain.request())
                .newBuilder()
                .code(200)
                .protocol(Protocol.HTTP_2)
                .message(response)
                .body(
                    ResponseBody.create(
                        MediaType.parse("application/json"),
                        response.toByteArray()
                    )
                )
                .addHeader("content-type", "application/json")
                .build()
        } else {
            return chain.proceed(chain.request())
                .newBuilder()
                .code(400)
                .protocol(Protocol.HTTP_2)
                .message(response)
                .body(
                    ResponseBody.create(
                        MediaType.parse("application/json"),
                        response.toByteArray()
                    )
                )
                .addHeader("content-type", "application/json")
                .build()
        }
    }
}