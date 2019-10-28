package com.assignment1.hillforts.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.assignment1.hillforts.R
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.lang.Exception


class SplashScreenActivity : Activity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val background = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)
                    val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                    startActivity(intent)
                    info("WORKING")
                } catch (e: Exception) {
                    info("NOT WORKING")
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}