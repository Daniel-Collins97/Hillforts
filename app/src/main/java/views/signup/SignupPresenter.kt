package views.signup

import android.app.Activity.RESULT_OK
import com.assignment1.hillforts.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import views.base.BasePresenter
import views.base.BaseView

class SignupPresenter(view: BaseView): BasePresenter(view), AnkoLogger {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun doUserAdd() {
        if (view?.userFirstName!!.text.isNotEmpty() && view?.userLastName!!.text.isNotEmpty() && view?.userEmail!!.text.isNotEmpty() && view?.userPassword!!.text.isNotEmpty() && view?.userPasswordConfirm!!.text.isNotEmpty()) {
            if (view?.userPassword!!.text.toString() == view?.userPasswordConfirm!!.text.toString()) {
                view?.showProgress()
                auth.createUserWithEmailAndPassword(view?.userEmail!!.text.toString(), view?.userPassword!!.text.toString()).addOnCompleteListener(view!!) { task ->
                    if (task.isSuccessful) {
                        view?.setResult(RESULT_OK)
                        view?.finish()
                    } else {
                        view?.toast("Sign Up Failed: ${task.exception?.message}")
                    }
                }
                view?.hideProgress()
            } else {
                view?.toast(R.string.passwords_do_not_match)
            }
        } else {
            view?.toast(R.string.toast_empty_fields)
        }
    }
}