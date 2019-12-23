package views.login

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.assignment1.hillforts.R
import views.signup.SignupView
import com.assignment1.hillforts.models.UserModel
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import views.base.BasePresenter
import views.base.BaseView
import views.base.VIEW

class LoginPresenter(view: BaseView): BasePresenter(view), AnkoLogger {

    private var user = UserModel()
    private var correctSet = false
    private var viewPassword = false

    fun doLogin() {
        val allUsers = app.users.findAllUsers()
        val userEmails = ArrayList<String>()
        user.email = view?.username!!.text.toString()
        user.password = view?.password!!.text.toString()
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
                    view?.navigateTo(VIEW.LIST, 0, "user", user, "", null)
                }
                user.email in userEmails -> Toast.makeText(
                    view!!,
                    R.string.user_incorrect_details,
                    Toast.LENGTH_LONG
                ).show()
                else -> noUserDialog()
            }
        } else {
            Toast.makeText(view!!, R.string.toast_empty_fields, Toast.LENGTH_LONG).show()
        }
    }

    private fun noUserDialog() {
        val builder = AlertDialog.Builder(view!!)
        builder.setTitle(R.string.dialog_no_user_title)
        builder.setMessage(R.string.dialog_no_user_message)
        builder.setPositiveButton(R.string.dialog_try_again) { _, _ ->
            view?.username!!.setText("")
            view?.password!!.setText("")
        }
        builder.setNegativeButton(R.string.dialog_sign_up) { _, _->
            view?.startActivityForResult(view?.intentFor<SignupView>(), 0)
        }
        builder.show()
    }

    fun doSignUp() {
        view?.startActivityForResult(view?.intentFor<SignupView>(), 0)
    }

    fun doPasswordView() {
        if (viewPassword) {
            view?.password!!.transformationMethod = PasswordTransformationMethod.getInstance()
            viewPassword = !viewPassword

        } else {
            view?.password!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
            viewPassword = !viewPassword
        }
    }

    fun doUserCreatedDialog() {
        val builder = AlertDialog.Builder(view!!)
        builder.setTitle(R.string.dialog_user_created)
        builder.setMessage(R.string.dialog_user_successful)
        builder.setPositiveButton(R.string.dialog_ok) { _, _ ->
        }
        builder.show()
    }
}