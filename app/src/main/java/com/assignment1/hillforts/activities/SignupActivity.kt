package com.assignment1.hillforts.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.assignment1.hillforts.R
import com.assignment1.hillforts.main.MainApp
import com.assignment1.hillforts.models.UserModel
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast


class SignupActivity : AppCompatActivity(), AnkoLogger {

    private var user = UserModel()
    lateinit var app : MainApp
    private val emailRegex = "^[A-Za-z](.*)([@])(.+)(\\.)(.+)"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        app = application as MainApp

        userAdd.setOnClickListener {
            info("userPassword ${userPassword.text}")
            info("userPasswordConfirm ${userPasswordConfirm.text}")
            if (userPassword.text.toString() == userPasswordConfirm.text.toString()) {
                user.firstName = userFirstName.text.toString()
                user.lastName = userLastName.text.toString()
                user.email = userEmail.text.toString()
                user.password = userPassword.text.toString()
                if (user.firstName.isNotEmpty() && user.lastName.isNotEmpty() && user.email.isNotEmpty() && user.password.isNotEmpty()) {
                    if (isEmailValid(user.email)) {
                        app.users.createUser(user.copy())
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        toast(R.string.invalid_email)
                    }
                } else {
                    toast(R.string.toast_empty_fields)
                }
            } else {
                toast(R.string.passwords_do_not_match)
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return emailRegex.toRegex().matches(email)
    }
}