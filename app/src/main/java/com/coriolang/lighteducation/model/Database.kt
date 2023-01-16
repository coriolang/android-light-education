package com.coriolang.lighteducation.model

import android.util.Log
import com.coriolang.lighteducation.model.data.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class Database {

    private val db = Firebase.database(DATABASE_URL).reference

    private val _topics = MutableStateFlow(emptyList<Topic>())
    val topics = _topics.asStateFlow()

    private val _directions = MutableStateFlow(emptyList<Direction>())
    val directions = _directions.asStateFlow()

    private val _subjects = MutableStateFlow(emptyList<String>())
    val subjects = _subjects.asStateFlow()

    private val _institutions = MutableStateFlow(emptyList<Institution>())
    val institutions = _institutions.asStateFlow()

    suspend fun writeUser(id: String, name: String, email: String) {
        val user = User(
            id = id,
            name = name,
            email = email,
            expert = false
        )

        db.child(USERS_KEY).child(user.id!!).setValue(user)
    }

    suspend fun startListenTopics(userId: String) {
        val topicsQuery = db.child(TOPICS_KEY)

        topicsQuery.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot
                    .getValue<Map<String, Topic>>()
                    ?.values?.toList()
                    ?.filter { topic ->
                        topic.userId == userId
                    }

                if (list != null) {
                    _topics.update { list }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "loadTopics:onCancelled", error.toException())
            }
        })
    }

    suspend fun getDirections(
        name: String? = null,
        subject: String? = null,
        level: Level? = null,
        money: Long? = null,
        format: Format? = null,
        score: Long? = null
    ) {
        if (subject != null) {
            db.child(INSTITUTIONS_KEY).get()
                .addOnSuccessListener { snapshot ->
                    Log.i(TAG, "Got value ${snapshot.value}")

                    val list = snapshot
                        .getValue<Map<String, Institution>>()
                        ?.values?.toList()

                    if (list != null) {
                        _institutions.update { list }
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, "Error getting data", it)
                }
        }

        db.child(DIRECTIONS_KEY).get()
            .addOnSuccessListener { snapshot ->
                Log.i(TAG, "Got value ${snapshot.value}")

                val list = snapshot
                    .getValue<Map<String, Direction>>()
                    ?.values?.toList()
                    ?.asSequence()
                    ?.filter { direction ->
                        if (name == null) {
                            true
                        } else {
                            val lowercaseName = name.lowercase()

                            direction.code!!.lowercase().contains(lowercaseName)
                                    || direction.name!!.lowercase().contains(lowercaseName)
                                    || "${direction.code} ${direction.name}".lowercase()
                                .contains(lowercaseName)
                        }
                    }
                    ?.filter { direction ->
                        if (subject == null) {
                            true
                        } else {
                            institutions.value.any { institution ->
                                institution.subject == subject
                                        && institution.id == direction.institutionId
                            }
                        }
                    }
                    ?.filter { direction ->
                        if (level == null) {
                            true
                        } else {
                            direction.level!! == level.toLong()
                        }
                    }
                    ?.filter { direction ->
                        if (money == null) {
                            true
                        } else {
                            direction.money!! == money
                        }
                    }
                    ?.filter { direction ->
                        if (format == null) {
                            true
                        } else {
                            direction.format!! == format.toLong()
                        }
                    }
                    ?.filter { direction ->
                        if (score == null) {
                            true
                        } else {
                            direction.score!! == score.toLong()
                        }
                    }?.toList()

                if (list != null) {
                    _directions.update { list }
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Error getting data", it)
            }
    }

    suspend fun getSubjects() {
        db.child(SUBJECTS_KEY).get()
            .addOnSuccessListener { snapshot ->
                Log.i(TAG, "Got value ${snapshot.value}")

                val list = snapshot
                    .getValue<Map<String, String>>()
                    ?.values?.toList()

                if (list != null) {
                    _subjects.update { list }
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Error getting data", it)
            }
    }

    companion object {
        private const val TAG = "Database"

        private const val DATABASE_URL =
            "https://the-light-of-education-default-rtdb.europe-west1.firebasedatabase.app/"

        private const val USERS_KEY = "users"
        private const val TOPICS_KEY = "topics"
        private const val SUBJECTS_KEY = "subjects"
        private const val INSTITUTIONS_KEY = "institutions"
        private const val DIRECTIONS_KEY = "directions"
    }
}