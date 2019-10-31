package com.assignment1.hillforts.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.assignment1.hillforts.R
import com.assignment1.hillforts.main.MainApp
import com.assignment1.hillforts.models.HillfortModel
import com.assignment1.hillforts.models.UserModel
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast


class SettingsActivity : AppCompatActivity(), AnkoLogger {

    private var user = UserModel()
    lateinit var app : MainApp
    private var allHillforts: List<HillfortModel>? = null
    private var visitedHillforts: List<HillfortModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        app = application as MainApp

        if (intent.hasExtra("user")) {
            user = intent.extras?.getParcelable("user")!!
        }

        allHillforts = loadHillforts()
        visitedHillforts = findVisitedHillforts()

        settingsEmail.text = user.email
        settingsPassword.text = user.password
        settingsStatisticsTotalHillforts.text = allHillforts!!.size.toString()
        settingsStatisticsVisited.text = visitedHillforts!!.size.toString()

        settingsLogOutBtn.setOnClickListener {
            val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadHillforts(): List<HillfortModel> {
        return app.hillforts.findAll().filter { it.userId == user.id }
    }

    private fun findVisitedHillforts() :List<HillfortModel> {
        val allHillforts = app.hillforts.findAll().filter { it.userId == user.id }
        return allHillforts.filter { it.visited }
    }
}