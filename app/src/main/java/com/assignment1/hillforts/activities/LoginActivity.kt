package com.assignment1.hillforts.activities

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.assignment1.hillforts.R
import com.assignment1.hillforts.main.MainApp
import com.assignment1.hillforts.models.UserModel
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast


class LoginActivity : AppCompatActivity(), AnkoLogger {

    private var user = UserModel()
    lateinit var app : MainApp
    private var correctSet = false
    private var viewPassword = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        app = application as MainApp


        loginButton.setOnClickListener {
            val allUsers = app.users.findAllUsers()
            val userEmails = ArrayList<String>()
            user.email = username.text.toString()
            user.password = password.text.toString()
            if (user.email.isNotEmpty()) {
                for (x in allUsers) {
                    userEmails.add(x.email)
                    if (x.password == user.password && x.email == user.email) {
                        user.lastName = x.lastName
                        user.firstName = x.firstName
                        user.id = x.id
                        correctSet = true
                    }
                }
                when {
                    correctSet -> {
                        val intent = Intent(this@LoginActivity, HillfortsListActivity::class.java)
                        intent.putExtra("user", user)
                        startActivity(intent)
                    }
                    user.email in userEmails -> toast(R.string.user_incorrect_details)
                    else -> noUserDialog()
                }
            }
            else {
                toast(R.string.toast_empty_fields)
            }
        }

        signupButton.setOnClickListener {
            startActivityForResult(intentFor<SignupActivity>(), 0)
        }

        passwordView.setOnClickListener {
            if (viewPassword) {
                password.transformationMethod = PasswordTransformationMethod.getInstance()
                viewPassword = !viewPassword

            } else {
                password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                viewPassword = !viewPassword
            }
        }

    }

    private fun noUserDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.dialog_no_user_title)
        builder.setMessage(R.string.dialog_no_user_message)
        builder.setPositiveButton(R.string.dialog_try_again) { _, _ ->
            username.setText("")
            password.setText("")
        }
        builder.setNegativeButton(R.string.dialog_sign_up) { _, _->
            startActivityForResult(intentFor<SignupActivity>(), 0)
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        userCreatedDialog()
        username.setText("")
        password.setText("")
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun userCreatedDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.dialog_user_created)
        builder.setMessage(R.string.dialog_user_successful)
        builder.setPositiveButton(R.string.dialog_ok) { _, _ ->
        }
        builder.show()
    }
}