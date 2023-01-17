package com.coriolang.lighteducation.ui.topic

data class MessageWithUser(
    val id: String = "",
    val userId: String = "",
    val username: String = "no-name user",
    val expert: Boolean = false,
    val text: String = "empty message",
    val timestamp: Long = 0L
)
