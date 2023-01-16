package com.coriolang.lighteducation.ui.home

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
import kotlinx.coroutines.launch

class HomeViewModel(
    private val database: Database
) : ViewModel() {

    val topics: Flow<List<Topic>>
        get() = database.topics

    fun startListenTopics(userId: String) {
        viewModelScope.launch {
            database.startListenTopicsByUser(userId)
        }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val database = (this[APPLICATION_KEY] as LeducationApp).database

                HomeViewModel(
                    database = database
                )
            }
        }
    }
}