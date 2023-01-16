package com.coriolang.lighteducation.model.data

import android.content.Context
import com.coriolang.lighteducation.R
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Direction(
    val id: String? = null,
    val institutionId: String? = null,
    val code: String? = null,
    val name: String? = null,
    val level: Long? = null,
    val format: Long? = null,
    val money: Long? = null,
    val score: Long? = null
)

enum class Level {
    MIDDLE,
    UNDERGRADUATE,
    MAGISTRACY,
    GRADUATE_SCHOOL
}

fun Level.toString(context: Context) = when (this) {
    Level.MIDDLE -> context.getString(R.string.middle)
    Level.UNDERGRADUATE -> context.getString(R.string.undergraduate)
    Level.MAGISTRACY -> context.getString(R.string.magistracy)
    Level.GRADUATE_SCHOOL -> context.getString(R.string.graduate_school)
}

fun Level.toLong() = when (this) {
    Level.MIDDLE -> 0L
    Level.UNDERGRADUATE -> 1L
    Level.MAGISTRACY -> 2L
    Level.GRADUATE_SCHOOL -> 3L
}

fun Long.toLevel() = when (this) {
    0L -> Level.MIDDLE
    1L -> Level.UNDERGRADUATE
    2L -> Level.MAGISTRACY
    else -> Level.GRADUATE_SCHOOL
}

enum class Format {
    FULL_TIME,
    PART_TIME,
    EVENING,
    REMOTE,
    ACCELERATED
}

fun Format.toString(context: Context) = when (this) {
    Format.FULL_TIME -> context.getString(R.string.full_time)
    Format.PART_TIME -> context.getString(R.string.part_time)
    Format.EVENING -> context.getString(R.string.evening)
    Format.REMOTE -> context.getString(R.string.remote)
    Format.ACCELERATED -> context.getString(R.string.accelerated)
}

fun Format.toLong() = when (this) {
    Format.FULL_TIME -> 0L
    Format.PART_TIME -> 1L
    Format.EVENING -> 2L
    Format.REMOTE -> 3L
    Format.ACCELERATED -> 4L
}

fun Long.toFormat() = when (this) {
    0L -> Format.FULL_TIME
    1L -> Format.PART_TIME
    2L -> Format.EVENING
    3L -> Format.REMOTE
    else -> Format.ACCELERATED
}