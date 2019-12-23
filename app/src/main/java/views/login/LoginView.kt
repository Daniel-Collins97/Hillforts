package views.login

import android.content.Intent
import android.os.Bundle
import com.assignment1.hillforts.R
import com.assignment1.hillforts.main.MainApp
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import views.base.BaseView


class LoginView : BaseView(), AnkoLogger {

    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        presenter = initPresenter(LoginPresenter(this)) as LoginPresenter


        loginButton.setOnClickListener {
            presenter.doLogin()
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
}