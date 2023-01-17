package com.coriolang.lighteducation.ui.topic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.coriolang.lighteducation.LeducationApp
import com.coriolang.lighteducation.model.Database
import com.coriolang.lighteducation.model.data.Topic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TopicViewModel(
    private val database: Database
) : ViewModel() {

    val topic: StateFlow<Topic>
        get() = database.topic

    val messages: Flow<List<MessageWithUser>>
        get() = database.messages

    fun getTopic(topicId: String) {
        viewModelScope.launch {
            database.getTopic(topicId)
        }
    }

    fun startListenMessages(topicId: String) {
        viewModelScope.launch {
            database.startListenUsers()
            database.startListenMessages(topicId)
        }
    }

    fun writeMessage(topicId: String, userId: String, text: String) {
        viewModelScope.launch {
            database.writeMessage(topicId, userId, text)
        }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val database = (this[APPLICATION_KEY] as LeducationApp).database

                TopicViewModel(
                    database = database
                )
            }
        }
    }
}