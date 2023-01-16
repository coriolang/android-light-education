package com.coriolang.lighteducation.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.coriolang.lighteducation.LeducationApp
import com.coriolang.lighteducation.model.Database
import com.coriolang.lighteducation.model.data.Direction
import com.coriolang.lighteducation.model.data.Format
import com.coriolang.lighteducation.model.data.Institution
import com.coriolang.lighteducation.model.data.Level
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val database: Database
) : ViewModel() {

    val directions: Flow<List<Direction>>
        get() = database.directions

    val subjects: StateFlow<List<String>>
        get() = database.subjects

    val institutions: StateFlow<List<Institution>>
        get() = database.institutions

    private var _name: String? = null
    var name: String?
        get() = _name
        set(value) {
            _name = value
        }

    private var _subject: String? = null
    var subject: String?
        get() = _subject
        set(value) {
            _subject = value
        }

    private var _level: Level? = null
    var level: Level?
        get() = _level
        set(value) {
            _level = value
        }

    private var _money: Long? = null
    var money: Long?
        get() = _money
        set(value) {
            _money = value
        }

    private var _format: Format? = null
    var format: Format?
        get() = _format
        set(value) {
            _format = value
        }

    private var _score: Long? = null
    var score: Long?
        get() = _score
        set(value) {
            _score = value
        }

    fun getDirections() {
        viewModelScope.launch {
            database.getDirections(
                name = _name,
                subject = _subject,
                level = _level,
                money = _money,
                format = _format,
                score = _score
            )
        }
    }

    fun getSubjects() {
        viewModelScope.launch {
            database.getSubjects()
        }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val database = (this[APPLICATION_KEY] as LeducationApp).database

                SearchViewModel(
                    database = database
                )
            }
        }
    }
}