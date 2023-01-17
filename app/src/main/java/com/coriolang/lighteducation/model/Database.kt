package com.coriolang.lighteducation.model

import android.util.Log
import com.coriolang.lighteducation.model.data.*
import com.coriolang.lighteducation.ui.topic.MessageWithUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

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

    private val _direction = MutableStateFlow(Direction())
    val direction = _direction.asStateFlow()

    private val _institution = MutableStateFlow(Institution())
    val institution = _institution.asStateFlow()

    private val _topicId = MutableStateFlow("")
    val topicId = _topicId.asStateFlow()

    private val _topic = MutableStateFlow(Topic())
    val topic = _topic.asStateFlow()

    private val _messages = MutableStateFlow(emptyList<MessageWithUser>())
    val messages = _messages.asStateFlow()

    private val _users = MutableStateFlow(emptyList<User>())
    private val users = _users.asStateFlow()

    suspend fun writeUser(id: String, name: String, email: String) {
        val user = User(
            id = id,
            name = name,
            email = email,
            expert = false
        )

        db.child(USERS_KEY).child(user.id!!).setValue(user)
    }

    suspend fun writeTopic(
        directionId: String,
        userId: String,
        title: String,
        text: String
    ) {
        val topic = Topic(
            id = UUID.randomUUID().toString(),
            directionId = directionId,
            userId = userId,
            title = title,
            text = text,
            timestamp = System.currentTimeMillis()
        )

        db.child(TOPICS_KEY).child(topic.id!!).setValue(topic)

        _topicId.update { topic.id }
    }

    suspend fun writeMessage(topicId: String, userId: String, text: String) {
        val message = Message(
            id = UUID.randomUUID().toString(),
            topicId = topicId,
            userId = userId,
            text = text,
            timestamp = System.currentTimeMillis()
        )

        db.child(MESSAGES_REF).child(message.id!!).setValue(message)
    }

    suspend fun startListenTopicsByUser(userId: String) {
        val topicsRef = db.child(TOPICS_KEY)

        topicsRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot
                    .getValue<Map<String, Topic>>()
                    ?.values?.toList()
                    ?.filter { topic ->
                        topic.userId == userId
                    }
                    ?.sortedBy { topic ->
                        topic.timestamp
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

    suspend fun startListenTopicsByDirection(directionId: String) {
        val topicsRef = db.child(TOPICS_KEY)

        topicsRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot
                    .getValue<Map<String, Topic>>()
                    ?.values?.toList()
                    ?.filter { topic ->
                        topic.directionId == directionId
                    }
                    ?.sortedBy { topic ->
                        topic.timestamp
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
                    ?.sortedBy { direction ->
                        direction.code
                    }

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
                    ?.sorted()

                if (list != null) {
                    _subjects.update { list }
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Error getting data", it)
            }
    }

    suspend fun getDirection(id: String) {
        db.child(DIRECTIONS_KEY).child(id).get()
            .addOnSuccessListener { snapshot ->
                Log.i(TAG, "Got value ${snapshot.value}")

                val direction = snapshot
                    .getValue<Direction>()

                if (direction != null) {
                    _direction.update { direction }
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Error getting data", it)
            }
    }

    suspend fun getInstitution(id: String) {
        db.child(INSTITUTIONS_KEY).child(id).get()
            .addOnSuccessListener { snapshot ->
                Log.i(TAG, "Got value ${snapshot.value}")

                val institution = snapshot
                    .getValue<Institution>()

                if (institution != null) {
                    _institution.update { institution }
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Error getting data", it)
            }
    }

    suspend fun getTopic(topicId: String) {
        db.child(TOPICS_KEY).child(topicId).get()
            .addOnSuccessListener { snapshot ->
                Log.i(TAG, "Got value ${snapshot.value}")

                val topic = snapshot
                    .getValue<Topic>()

                if (topic != null) {
                    _topic.update { topic }
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Error getting data", it)
            }
    }

    suspend fun startListenMessages(topicId: String) {
        val messagesRef = db.child(MESSAGES_REF)

        messagesRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot
                    .getValue<Map<String, Message>>()
                    ?.values?.toList()
                    ?.filter { message ->
                        message.topicId == topicId
                    }
                    ?.map { message ->
                        val user = users.value
                            .find { it.id == message.userId }

                        if (user != null) {
                            MessageWithUser(
                                id = message.id!!,
                                userId = user.id!!,
                                username = user.name!!,
                                expert = user.expert!!,
                                text = message.text!!,
                                timestamp = message.timestamp!!
                            )
                        } else {
                            MessageWithUser()
                        }
                    }
                    ?.sortedByDescending { message ->
                        message.timestamp
                    }

                if (list != null) {
                    _messages.update { list }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "loadMessages:onCancelled", error.toException())
            }
        })
    }

    suspend fun startListenUsers() {
        val usersRef = db.child(USERS_KEY)

        usersRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot
                    .getValue<Map<String, User>>()
                    ?.values?.toList()

                if (list != null) {
                    _users.update { list }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "loadUsers:onCancelled", error.toException())
            }
        })
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
        private const val MESSAGES_REF = "messages"
    }
}