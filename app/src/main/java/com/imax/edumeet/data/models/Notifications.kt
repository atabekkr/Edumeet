package com.imax.edumeet.data.models

data class Notifications(
    val notifications: List<Notification>,
    val averageRating: Int
)

data class Notification(
    val __v: Int,
    val _id: String,
    val feedback: String,
    val from: From,
    val rate: Int,
    val read: Boolean,
    val stream: StreamData,
    val student: String
)

data class From(
    val name: String,
    val profileImage: String,
    val science: String
)

data class StreamData(
    val name: String,
    val profileImage: String,
    val streamId: String,
    val title: String
)