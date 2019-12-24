package views.settings

import android.annotation.SuppressLint
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.assignment1.hillforts.R
import com.assignment1.hillforts.models.HillfortModel
import com.assignment1.hillforts.models.UserModel
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import views.base.BasePresenter
import views.base.BaseView
import views.base.VIEW

class SettingsPresenter(view: BaseView): BasePresenter(view), AnkoLogger {

    private var user = UserModel()
    private var allHillforts: List<HillfortModel>? = null
    private var visitedHillforts: List<HillfortModel>? = null
    private val emailRegex = "^[A-Za-z](.*)([@])(.+)(\\.)(.+)"
    private var updateUser = UserModel()

    init {
        if (view.intent.hasExtra("user")) {
            user = view.intent.extras?.getParcelable("user")!!
        }
        doAsync{
            loadHillforts()
            uiThread {
                view.settingsEmail.text = user.email
                view.settingsPassword.text = user.password
                view.settingsStatisticsTotalHillforts.text = allHillforts!!.size.toString()
                view.settingsStatisticsVisited.text = visitedHillforts!!.size.toString()
            }
        }
    }

    private fun loadHillforts() {
        allHillforts = app.hillforts.findAllHillforts().filter { it.userId == user.id }
        findVisitedHillforts()
    }

    private fun findVisitedHillforts() {
        visitedHillforts = allHillforts!!.filter { it.visited }
    }

    fun doLogOut() {
        view?.navigateTo(VIEW.LOGIN, 0, "", null, "", null)
    }

    fun doEditEmail() {
        editEmailDialog()
    }

    fun doEditPassword() {
        editPasswordDialog()
    }

    @SuppressLint("InflateParams")
    private fun editEmailDialog() {
        val builder = AlertDialog.Builder(view!!).create()
        val inflater = view?.layoutInflater
        val dialogView = inflater?.inflate(R.layout.edit_email_dialog, null)

        val newEmail = dialogView?.findViewById(R.id.edit_email) as EditText
        val button1 = dialogView.findViewById(R.id.buttonSubmit) as Button
        val button2 = dialogView.findViewById(R.id.buttonCancel) as Button


        button1.setOnClickListener {
            if (isEmailValid(newEmail.text.toString())) {
                updateUser.email = newEmail.text.toString()
                updateUser.firstName = user.firstName
                updateUser.lastName = user.lastName
                updateUser.password = user.password
                updateUser.id = user.id
                doAsync {
                    app.users.updateUser(updateUser.copy())
                    uiThread {
                        builder.dismiss()
                        view?.settingsEmail!!.text = newEmail.text.toString()
                    }
                }
            } else {
                Toast.makeText(view, R.string.invalid_email, Toast.LENGTH_LONG).show()
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

    @SuppressLint("InflateParams")
    private fun editPasswordDialog() {
        val builder = AlertDialog.Builder(view!!).create()
        val inflater = view?.layoutInflater
        val dialogView = inflater?.inflate(R.layout.edit_password_dialog, null)

        val newPassword = dialogView?.findViewById(R.id.edit_password) as EditText
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
                doAsync {
                    app.users.updateUser(updateUser.copy())
                    uiThread {
                        builder.dismiss()
                        view?.settingsPassword!!.text = newPassword.text.toString()
                    }
                }
            } else {
                Toast.makeText(view, R.string.passwords_do_not_match, Toast.LENGTH_LONG).show()
            }
        }

        button2.setOnClickListener {
            builder.dismiss()
        }

        builder.setView(dialogView)
        builder.show()
    }
}