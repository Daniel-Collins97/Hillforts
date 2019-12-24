package com.assignment1.hillforts.main

import android.app.Application
import com.assignment1.hillforts.models.*
import com.assignment1.hillforts.room.HillfortStoreRoom
import com.assignment1.hillforts.room.UserStoreRoom
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

    lateinit var hillforts: HillfortStore
    lateinit var users: UserStore

    override fun onCreate() {
        super.onCreate()
        hillforts = HillfortStoreRoom(applicationContext)
        users = UserStoreRoom(applicationContext)
        info("Hillforts started")
    }
}