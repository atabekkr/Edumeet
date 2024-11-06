package com.imax.edumeet.utils

import com.imax.edumeet.BuildConfig

object Constants {
    const val LISTENING = 1
    const val READING = 2
    const val WRITING = 3
    const val SPEAKING = 4

    const val CLASSIC = 1
    const val TRUEFALSE = 2
    const val MATCHING = 3

    const val BASE_URL = "https://stream-service-api.vercel.app"

    const val OPEN_AI_API_KEY = BuildConfig.OPEN_AI_KEY
}