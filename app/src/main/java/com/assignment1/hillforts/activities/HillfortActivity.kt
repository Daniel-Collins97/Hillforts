package com.assignment1.hillforts.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import com.assignment1.hillforts.R
import com.assignment1.hillforts.main.MainApp
import com.assignment1.hillforts.models.HillfortModel

class HillfortActivity : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortModel()
    val hillforts= ArrayList<HillfortModel>()
    lateinit var app : MainApp


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        app = application as MainApp


        btnAdd.setOnClickListener() {
            hillfort.title = hillfortTitle.text.toString()
            hillfort.locationLat = hillfortLocationLat.text.toString()
            hillfort.locationLong = hillfortLocationLong.text.toString()
            if (hillfort.title.isNotEmpty() && hillfort.locationLat.isNotEmpty() && hillfort.locationLong.isNotEmpty()) {
                app.hillforts.add(hillfort.copy())
                info("add Button Pressed: $hillfort")
                for (i in app!!.hillforts.indices) {
                    info("Hillfort[$i]:${app.hillforts[i]}")
                }
            }
            else {
                toast ("Please fill in ALL fields")
            }
        }
    }
}