package com.coriolang.lighteducation.model.data

data class Topic(
    val id: String,
    val directionId: String,
    val userId: String,
    val title: String,
    val text: String,
    val timestamp: Long
)
