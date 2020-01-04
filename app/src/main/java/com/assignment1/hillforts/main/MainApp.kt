package com.assignment1.hillforts.main

import android.app.Application
import com.assignment1.hillforts.firebase.HillfortFireStore
import com.assignment1.hillforts.models.*
import com.assignment1.hillforts.room.HillfortStoreRoom
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

    lateinit var hillforts: HillfortStore

    override fun onCreate() {
        super.onCreate()
        hillforts = HillfortFireStore(applicationContext)
        info("Hillforts started")
    }
}