package com.imax.edumeet.data.ai

import android.util.Log
import com.imax.edumeet.utils.Constants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val baseUrl = "https://api.openai.com/v1/audio/transcriptions"
private const val apiKey = Constants.OPEN_AI_API_KEY

class WhisperApiImpl(
    private val client: HttpClient
) : WhisperApi {
    override suspend fun transcribe(
        bytes: ByteArray,
        filename: String
    ): Result<TranscriptionResult> = runCatching {
        val response: HttpResponse =
            client.submitFormWithBinaryData(url = baseUrl, formData = formData {
                append("file", bytes, audioHeaders(filename))
                append("model", "whisper-1")
                append("response_format", "verbose_json")
            }) {
                bearerAuth(apiKey)
            }
        Log.d("WhisperApi", "${response.body<TranscriptionResult>()}")
        response.body<TranscriptionResult>()
    }

    private fun audioHeaders(filename: String) = Headers.build {
        append(HttpHeaders.ContentDisposition, "filename=$filename")
    }
}

interface WhisperApi {
    suspend fun transcribe(bytes: ByteArray, filename: String): Result<TranscriptionResult>
}

fun createHttpClient(json: Json) = HttpClient {
    install(ContentNegotiation) {
        json(json)
    }
    install(Logging) {
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                Log.d( "WhisperApi", message)
            }
        }
    }
    defaultRequest {
        headers {
            append(HttpHeaders.ContentType, "multipart/form-data")
        }
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 60000L
        connectTimeoutMillis = 60000L
    }
}
