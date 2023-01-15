package com.coriolang.lighteducation

import android.app.Application
import com.coriolang.lighteducation.model.Authentication
import com.coriolang.lighteducation.model.Database

class LeducationApp : Application() {

    lateinit var authentication: Authentication
    lateinit var database: Database

    override fun onCreate() {
        super.onCreate()

        authentication = Authentication()
        database = Database()
    }
}