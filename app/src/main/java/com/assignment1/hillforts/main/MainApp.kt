package com.assignment1.hillforts.main

import android.app.Application
import com.assignment1.hillforts.models.HillfortModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

    val hillforts = ArrayList<HillfortModel>()

    override fun onCreate() {
        super.onCreate()
        info("Hillfort started")
        hillforts.add(HillfortModel("One", "-87.435678", "6.45678"))
        hillforts.add(HillfortModel("Two", "-73.65409876", "987.435678"))
        hillforts.add(HillfortModel("Three", "-324.765438", "-65.8946789"))
    }
}