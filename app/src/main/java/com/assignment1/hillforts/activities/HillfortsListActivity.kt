package com.assignment1.hillforts.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.assignment1.hillforts.R
import com.assignment1.hillforts.main.MainApp

class HillfortsListActivity : AppCompatActivity() {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillforts_list)
        app = application as MainApp
    }
}