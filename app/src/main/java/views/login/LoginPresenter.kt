package views.login

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.assignment1.hillforts.R
import com.assignment1.hillforts.firebase.HillfortFireStore
import views.signup.SignupView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*
import views.base.BasePresenter
import views.base.BaseView
import views.base.VIEW



class LoginPresenter(view: BaseView): BasePresenter(view) {

    private var viewPassword = false
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var fireStore: HillfortFireStore? = null

    init {
        if (app.hillforts is HillfortFireStore) {
            fireStore = app.hillforts as HillfortFireStore
        }
    }

    fun doLogin(email: String, password: String) {
        if(view?.username!!.text.isNotEmpty() && view?.password!!.text.isNotEmpty()) {
            view?.showProgress()
            view?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
                if (task.isSuccessful) {
                    if (fireStore != null) {
                        fireStore!!.fetchHillforts {
                            val user = FirebaseAuth.getInstance().currentUser
                            view?.navigateTo(VIEW.LIST, 0, "user", user, "", null)
                        }
                    } else {
                        val user = FirebaseAuth.getInstance().currentUser
                        view?.navigateTo(VIEW.LIST, 0, "user", user, "", null)
                    }
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