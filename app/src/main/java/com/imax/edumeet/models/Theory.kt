package com.imax.edumeet.models

data class Theory(
    val theory: List<TheorySection>
)

data class TheorySection(
    val id: Int,
    val name: String,
    val topics: List<TopicWithContent>
)

data class TopicWithContent(
    val topic_id: Int,
    val content: List<Content>
)

data class Content(
    val type: String,
    val body: Any
)