package views.signup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.assignment1.hillforts.R
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.AnkoLogger
import views.base.BaseView

class SignupView : BaseView(), AnkoLogger {

    private lateinit var presenter: SignupPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        presenter = initPresenter(SignupPresenter(this)) as SignupPresenter

        userAdd.setOnClickListener {
            presenter.doUserAdd()
        }
    }
}