package views.signup

import android.app.Activity.RESULT_OK
import android.widget.Toast
import com.assignment1.hillforts.R
import com.assignment1.hillforts.models.UserModel
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import views.base.BasePresenter
import views.base.BaseView

class SignupPresenter(view: BaseView): BasePresenter(view), AnkoLogger {

    private var user = UserModel()
    private val emailRegex = "^[A-Za-z](.*)([@])(.+)(\\.)(.+)"

    fun doUserAdd() {
        if (view?.userPassword!!.text.toString() == view?.userPasswordConfirm!!.text.toString()) {
            user.firstName = view?.userFirstName!!.text.toString()
            user.lastName = view?.userLastName!!.text.toString()
            user.email = view?.userEmail!!.text.toString()
            user.password = view?.userPassword!!.text.toString()
            if (user.firstName.isNotEmpty() && user.lastName.isNotEmpty() && user.email.isNotEmpty() && user.password.isNotEmpty()) {
                if (isEmailValid(user.email)) {
                    doAsync {
                        app.users.createUser(user.copy())
                        uiThread {
                            view?.setResult(RESULT_OK)
                            view?.finish()
                        }
                    }
                } else {
                    Toast.makeText(view, R.string.invalid_email, Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(view, R.string.toast_empty_fields, Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(view, R.string.passwords_do_not_match, Toast.LENGTH_LONG).show()
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return emailRegex.toRegex().matches(email)
    }
}