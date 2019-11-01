package com.assignment1.hillforts.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.assignment1.hillforts.R
import com.assignment1.hillforts.main.MainApp
import com.assignment1.hillforts.models.HillfortModel
import com.assignment1.hillforts.models.UserModel
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.AnkoLogger
import android.widget.EditText
import android.widget.Button
import org.jetbrains.anko.toast


class SettingsActivity : AppCompatActivity(), AnkoLogger {

    private var user = UserModel()
    lateinit var app : MainApp
    private var allHillforts: List<HillfortModel>? = null
    private var visitedHillforts: List<HillfortModel>? = null
    private val emailRegex = "^[A-Za-z](.*)([@])(.+)(\\.)(.+)"
    private var updateUser = UserModel()

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

        editEmail.setOnClickListener {
            editEmailDialog()
        }

        editPassword.setOnClickListener {
            editPasswordDialog()
        }
    }

    @SuppressLint("InflateParams")
    private fun editEmailDialog() {
        val builder = AlertDialog.Builder(this).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.edit_email_dialog, null)

        val newEmail = dialogView.findViewById(R.id.edit_email) as EditText
        val button1 = dialogView.findViewById(R.id.buttonSubmit) as Button
        val button2 = dialogView.findViewById(R.id.buttonCancel) as Button


        button1.setOnClickListener {
            if (isEmailValid(newEmail.text.toString())) {
                updateUser.email = newEmail.text.toString()
                updateUser.firstName = user.firstName
                updateUser.lastName = user.lastName
                updateUser.password = user.password
                updateUser.id = user.id
                app.users.updateUser(updateUser.copy())
                builder.dismiss()
                settingsEmail.text = newEmail.text.toString()
            } else {
                toast(R.string.invalid_email)
            }
        }

        button2.setOnClickListener {
                builder.dismiss()
        }

        builder.setView(dialogView)
        builder.show()
    }

    @SuppressLint("InflateParams")
    private fun editPasswordDialog() {
        val builder = AlertDialog.Builder(this).create()
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.edit_password_dialog, null)

        val newPassword = dialogView.findViewById(R.id.edit_password) as EditText
        val newPasswordConfirm = dialogView.findViewById(R.id.edit_password_confirm) as EditText
        val button1 = dialogView.findViewById(R.id.buttonSubmit) as Button
        val button2 = dialogView.findViewById(R.id.buttonCancel) as Button


        button1.setOnClickListener {
            if (newPassword.text.toString() == newPasswordConfirm.text.toString()) {
                updateUser.email = user.email
                updateUser.firstName = user.firstName
                updateUser.lastName = user.lastName
                updateUser.password = newPassword.text.toString()
                updateUser.id = user.id
                app.users.updateUser(updateUser.copy())
                builder.dismiss()
                settingsPassword.text = newPassword.text.toString()
            } else {
                toast(R.string.passwords_do_not_match)
            }
        }

        button2.setOnClickListener {
            builder.dismiss()
        }

        builder.setView(dialogView)
        builder.show()
    }

    private fun isEmailValid(email: String): Boolean {
        return emailRegex.toRegex().matches(email)
    }

    private fun loadHillforts(): List<HillfortModel> {
        return app.hillforts.findAll().filter { it.userId == user.id }
    }

    private fun findVisitedHillforts() :List<HillfortModel> {
        val allHillforts = app.hillforts.findAll().filter { it.userId == user.id }
        return allHillforts.filter { it.visited }
    }
}