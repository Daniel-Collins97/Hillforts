package views.settings

import android.annotation.SuppressLint
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.assignment1.hillforts.R
import com.assignment1.hillforts.models.HillfortModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import views.base.BasePresenter
import views.base.BaseView
import views.base.VIEW

class SettingsPresenter(view: BaseView): BasePresenter(view) {

    private val user = FirebaseAuth.getInstance().currentUser
    private var allHillforts: List<HillfortModel>? = null
    private var visitedHillforts: List<HillfortModel>? = null

    init {
        doAsync{
            loadHillforts()
            uiThread {
                view.settingsEmail.text = user?.email.toString()
                view.settingsStatisticsTotalHillforts.text = allHillforts!!.size.toString()
                view.settingsStatisticsVisited.text = visitedHillforts!!.size.toString()
            }
        }
    }

    private fun loadHillforts() {
        allHillforts = app.hillforts.findAllHillforts().filter { it.userId == user?.uid.toString() }
        findVisitedHillforts()
    }

    private fun findVisitedHillforts() {
        visitedHillforts = allHillforts!!.filter { it.visited }
    }

    fun doLogOut() {
        FirebaseAuth.getInstance().signOut()
        app.hillforts.clear()
        view?.navigateTo(VIEW.LOGIN, 0, "", null, "", null)
    }

    fun doEditEmail() {
        passwordPrompt("email")
    }

    fun doEditPassword() {
        passwordPrompt("password")
    }

    @SuppressLint("InflateParams")
    private fun changeEmailPrompt() {
        val builder = AlertDialog.Builder(view!!).create()
        val inflater = view?.layoutInflater
        val dialogView = inflater?.inflate(R.layout.edit_email_dialog, null)

        val newEmail = dialogView?.findViewById(R.id.edit_email) as EditText
        val button1 = dialogView.findViewById(R.id.buttonSubmit) as Button
        val button2 = dialogView.findViewById(R.id.buttonCancel) as Button


        button1.setOnClickListener {
            if (newEmail.text.toString().isNotEmpty()) {
                user?.updateEmail(newEmail.text.toString())?.addOnCompleteListener(view!!) { task ->
                    if (task.isSuccessful) {
                        view?.toast("Email Updated")
                        view?.settingsEmail!!.text = user.email.toString()
                        builder.dismiss()
                    } else {
                        view?.toast("Email Updated Failed: ${task.exception?.message}")
                        builder.dismiss()
                    }
                }
            } else {
                view?.toast(R.string.toast_empty_fields)
            }
        }

        button2.setOnClickListener {
            builder.dismiss()
        }

        builder.setView(dialogView)
        builder.show()
    }

    private fun reauthenticate(email: String, password: String, detail: String) {
        val credential = EmailAuthProvider.getCredential(email, password)
        user?.reauthenticate(credential)?.addOnCompleteListener(view!!) { task ->
            if(task.isSuccessful) {
                when (detail) {
                    "email" -> {
                                changeEmailPrompt()
                                view?.hideProgress()
                                view?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                            }
                    "password" -> {
                                changePasswordPrompt()
                                view?.hideProgress()
                                view?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                            }
                }
            } else {
                view?.toast("Re-Authentication Failed: ${task.exception?.message}")
                view?.toast(R.string.authentication_failed)
                doLogOut()
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun passwordPrompt(detail: String) {
        val builder = AlertDialog.Builder(view!!).create()
        val inflater = view?.layoutInflater
        val dialogView = inflater?.inflate(R.layout.password_prompt_dialog, null)

        val password = dialogView?.findViewById(R.id.userPassword) as EditText
        val button1 = dialogView.findViewById(R.id.buttonSubmit) as Button
        val button2 = dialogView.findViewById(R.id.buttonCancel) as Button

        button1.setOnClickListener {
            if (password.text.toString().isNotEmpty()) {
                view?.showProgress()
                view?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                reauthenticate(user?.email.toString(), password.text.toString(), detail)
                builder.dismiss()
            } else {
                view?.toast(R.string.toast_empty_fields)
            }
        }

        button2.setOnClickListener {
            builder.dismiss()
        }

        builder.setView(dialogView)
        builder.show()
    }

    @SuppressLint("InflateParams")
    private fun changePasswordPrompt() {
        val builder = AlertDialog.Builder(view!!).create()
        val inflater = view?.layoutInflater
        val dialogView = inflater?.inflate(R.layout.edit_password_dialog, null)

        val password = dialogView?.findViewById(R.id.edit_password) as EditText
        val passwordConfirm = dialogView.findViewById(R.id.edit_password_confirm) as EditText
        val button1 = dialogView.findViewById(R.id.buttonSubmit) as Button
        val button2 = dialogView.findViewById(R.id.buttonCancel) as Button

        button1.setOnClickListener {
            if (password.text.toString().isNotEmpty() && passwordConfirm.text.toString().isNotEmpty()) {
                if (password.text.toString() == passwordConfirm.text.toString()) {
                    user?.updatePassword(password.text.toString())
                        ?.addOnCompleteListener(view!!) { task ->
                            if (task.isSuccessful) {
                                view?.toast("Password Updated")
                                builder.dismiss()
                            } else {
                                view?.toast("Password Updated Failed: ${task.exception?.message}")
                                builder.dismiss()
                            }
                        }
                } else {
                    view?.toast(R.string.passwords_do_not_match)
                    password.setText("")
                    passwordConfirm.setText("")
                }
            } else {
                view?.toast(R.string.toast_empty_fields)
            }
        }

        button2.setOnClickListener {
            builder.dismiss()
        }

        builder.setView(dialogView)
        builder.show()
    }
}