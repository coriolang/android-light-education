package com.coriolang.lighteducation.model.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Institution(
    val id: String? = null,
    val subject: String? = null,
    val name: String? = null,
    val address: String? = null
)
