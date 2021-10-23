package com.noah.scorereporter.fake

import com.google.gson.Gson
import com.noah.scorereporter.TestConstants
import okhttp3.*

class MockPageInterceptor(private val valid: Boolean) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val gson = Gson()
        val uri = chain.request().url().uri().toString()
        var response = ""

        response = when {
            uri.endsWith("/team/${TestConstants.TEAM_RESPONSE.id}") -> {
                if (valid) {
                    gson.toJson(
                        TestConstants.TEAM_RESPONSE
                    )
                } else {
                    TestConstants.TEAM_ERROR
                }
            }
            uri.endsWith("/follow") -> {
                if (valid) {
                    gson.toJson(
                        TestConstants.TEAM_RESPONSE
                    )
                } else {
                    TestConstants.TEAM_ERROR
                }
            }
            uri.matches(Regex("https://score-reporter.herokuapp.com/season/.+")) -> {
                if (valid) {
                    when {
                        uri.contains(TestConstants.SEASON_RESPONSE.id) -> {
                            gson.toJson(
                                TestConstants.SEASON_RESPONSE
                            )
                        }
                        uri.contains(TestConstants.SEASON_RESPONSE_2.id) -> {
                            gson.toJson(
                                TestConstants.SEASON_RESPONSE_2
                            )
                        }
                        else -> {
                            TestConstants.SEASON_ERROR
                        }
                    }
                } else {
                    TestConstants.SEASON_ERROR
                }
            }
            uri.matches(Regex("https://score-reporter.herokuapp.com/user/.+")) -> {
                if (valid) {
                    when {
                        uri.contains(TestConstants.USER_PROFILE_1.id) -> {
                            gson.toJson(
                                TestConstants.USER_PROFILE_1
                            )
                        }
                        uri.contains(TestConstants.USER_PROFILE_2.id) -> {
                            gson.toJson(
                                TestConstants.USER_PROFILE_2
                            )
                        }
                        else -> TestConstants.USER_ERROR
                    }
                } else {
                    TestConstants.USER_ERROR
                }
            }
            uri.matches(Regex("https://score-reporter.herokuapp.com/game/.+")) -> {
                if (valid) {
                    when {
                        uri.contains(TestConstants.GAME_1.id) -> {
                            gson.toJson(
                                TestConstants.GAME_1
                            )
                        }
                        uri.contains(TestConstants.GAME_2.id) -> {
                            gson.toJson(
                                TestConstants.GAME_2
                            )
                        }
                        else -> {
                            TestConstants.GAME_ERROR
                        }
                    }
                } else {
                    TestConstants.GAME_ERROR
                }
            }
            else -> {
                TestConstants.TEAM_ERROR
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