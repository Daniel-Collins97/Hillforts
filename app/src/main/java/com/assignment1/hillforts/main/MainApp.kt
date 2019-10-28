package com.assignment1.hillforts.main

import android.app.Application
import com.assignment1.hillforts.models.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

    lateinit var hillforts: HillfortStore
    lateinit var users: UserStore

    override fun onCreate() {
        super.onCreate()
        hillforts = HillfortJSONStore(applicationContext)
        users = UserJSONStore(applicationContext)
//        users = UserMemStore()
        info("Hillforts started")
    }
}