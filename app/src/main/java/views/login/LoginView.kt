package views.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.assignment1.hillforts.R
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import views.base.BaseView


class LoginView : BaseView(), AnkoLogger {

    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        presenter = initPresenter(LoginPresenter(this)) as LoginPresenter
        progressBar.visibility = View.GONE



        loginButton.setOnClickListener {
            presenter.doLogin(username.text.toString(), password.text.toString())
        }

        signupButton.setOnClickListener {
            presenter.doSignUp()
        }

        passwordView.setOnClickListener {
            presenter.doPasswordView()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.doUserCreatedDialog()
        username.setText("")
        password.setText("")
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }
}