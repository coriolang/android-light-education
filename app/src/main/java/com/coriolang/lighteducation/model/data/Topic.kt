package com.coriolang.lighteducation.model.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Topic(
    val id: String? = null,
    val directionId: String? = null,
    val userId: String? = null,
    val title: String? = null,
    val text: String? = null,
    val timestamp: Long? = null
)
