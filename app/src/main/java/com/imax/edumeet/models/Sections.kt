package com.imax.edumeet.models

data class Sections(
    val sections: List<Section>
)

data class Section(
    val id: Int,
    val name: String,
    val topics: List<Topic>
)

data class Topic(
    val id: Int,
    val name: String,
    val image: String
)
