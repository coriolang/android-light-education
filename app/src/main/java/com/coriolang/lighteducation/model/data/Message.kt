package com.coriolang.lighteducation.model.data

data class Message(
    val id: String,
    val topicId: String,
    val userId: String,
    val text: String,
    val timestamp: Long
)
