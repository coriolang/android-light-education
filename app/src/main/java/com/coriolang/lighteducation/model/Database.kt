package com.coriolang.lighteducation.model

import com.coriolang.lighteducation.model.data.User
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Database {

    private val db = Firebase.database(DATABASE_URL).reference

    suspend fun writeUser(id: String, name: String, email: String) {
        val user = User(
            id = id,
            name = name,
            email = email,
            expert = false
        )

        db.child(USERS_KEY).child(user.id!!).setValue(user)
    }

    companion object {
        private const val DATABASE_URL = "https://the-light-of-education-default-rtdb.europe-west1.firebasedatabase.app/"

        private const val USERS_KEY = "users"
    }
}