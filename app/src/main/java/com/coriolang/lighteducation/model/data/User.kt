package com.coriolang.lighteducation.model.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val expert: Boolean? = null
)
