package com.coriolang.lighteducation

import android.app.Application
import com.coriolang.lighteducation.data.Authentication

class LeducationApp : Application() {

    lateinit var authentication: Authentication

    override fun onCreate() {
        super.onCreate()
        authentication = Authentication()
    }
}