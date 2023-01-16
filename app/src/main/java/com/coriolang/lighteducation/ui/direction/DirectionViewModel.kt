package com.coriolang.lighteducation.ui.direction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.coriolang.lighteducation.LeducationApp
import com.coriolang.lighteducation.model.Database
import com.coriolang.lighteducation.model.data.Direction
import com.coriolang.lighteducation.model.data.Institution
import com.coriolang.lighteducation.model.data.Topic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DirectionViewModel(
    private val database: Database
) : ViewModel() {

    val topics: Flow<List<Topic>>
        get() = database.topics

    val direction: StateFlow<Direction>
        get() = database.direction

    val institution: StateFlow<Institution>
        get() = database.institution

    val topicId: StateFlow<String>
        get() = database.topicId

    fun writeTopic(
        directionId: String,
        userId: String,
        title: String,
        text: String
    ) {
        viewModelScope.launch {
            database.writeTopic(
                directionId,
                userId,
                title,
                text
            )
        }
    }

    fun startListenTopics(directionId: String) {
        viewModelScope.launch {
            database.startListenTopicsByDirection(directionId)
        }
    }

    fun getDirection(directionId: String) {
        viewModelScope.launch {
            database.getDirection(directionId)
        }
    }

    fun getInstitution(institutionId: String) {
        viewModelScope.launch {
            database.getInstitution(institutionId)
        }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val database = (this[APPLICATION_KEY] as LeducationApp).database

                DirectionViewModel(
                    database = database
                )
            }
        }
    }
}