package com.imax.edumeet.data.ai

data class ChatCompletionRequest(
    val model: String,
    val messages: List<Message>
)

data class Message(
    val role: String,
    val content: String
)

// ChatCompletionResponse.kt
data class ChatCompletionResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)