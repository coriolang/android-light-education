package com.coriolang.lighteducation.model.data

import android.content.Context
import com.coriolang.lighteducation.R

data class Direction(
    val id: String,
    val institutionId: String,
    val code: String,
    val name: String,
    val level: Int,
    val format: Int,
    val money: Int,
    val score: Int
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

fun Level.toInt() = when (this) {
    Level.MIDDLE -> 0
    Level.UNDERGRADUATE -> 1
    Level.MAGISTRACY -> 2
    Level.GRADUATE_SCHOOL -> 3
}

fun Int.toLevel() = when (this) {
    0 -> Level.MIDDLE
    1 -> Level.UNDERGRADUATE
    2 -> Level.MAGISTRACY
    3 -> Level.GRADUATE_SCHOOL
    else -> -1
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

fun Format.toInt() = when (this) {
    Format.FULL_TIME -> 0
    Format.PART_TIME -> 1
    Format.EVENING -> 2
    Format.REMOTE -> 3
    Format.ACCELERATED -> 4
}

fun Int.toFormat() = when (this) {
    0 -> Format.FULL_TIME
    1 -> Format.PART_TIME
    2 -> Format.EVENING
    3 -> Format.REMOTE
    4 -> Format.ACCELERATED
    else -> -1
}