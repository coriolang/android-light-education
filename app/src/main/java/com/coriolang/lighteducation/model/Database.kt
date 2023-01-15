package com.coriolang.lighteducation.model

import android.util.Log
import com.coriolang.lighteducation.model.data.Topic
import com.coriolang.lighteducation.model.data.User
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
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list = dataSnapshot
                    .getValue<Map<String, Topic>>()
                    ?.values?.toList()
                    ?.filter { topic ->
                        topic.userId == userId
                    }

                if (list != null) {
                    _topics.update { list }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadTopics:onCancelled", databaseError.toException())
            }
        })
    }

    companion object {
        private const val TAG = "Database"

        private const val DATABASE_URL = "https://the-light-of-education-default-rtdb.europe-west1.firebasedatabase.app/"

        private const val USERS_KEY = "users"
        private const val TOPICS_KEY = "topics"
    }
}