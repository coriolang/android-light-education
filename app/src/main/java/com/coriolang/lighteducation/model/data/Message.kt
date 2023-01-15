package com.coriolang.lighteducation.model.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(
    val id: String? = null,
    val topicId: String? = null,
    val userId: String? = null,
    val text: String? = null,
    val timestamp: Long? = null
)
