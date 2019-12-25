package views.login

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.assignment1.hillforts.R
import views.signup.SignupView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*
import views.base.BasePresenter
import views.base.BaseView
import views.base.VIEW



class LoginPresenter(view: BaseView): BasePresenter(view), AnkoLogger {

    private var viewPassword = false
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun doLogin(email: String, password: String) {
        if(view?.username!!.text.isNotEmpty() && view?.password!!.text.isNotEmpty()) {
            view?.showProgress()
            view?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    view?.navigateTo(VIEW.LIST, 0, "user", user, "", null)
                } else {
                    view?.toast("Login Unsuccessful: ${task.exception?.message}")
                }
                view?.hideProgress()
                view?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        } else {
            view?.toast(R.string.toast_empty_fields)
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