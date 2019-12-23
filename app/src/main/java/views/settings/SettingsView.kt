package views.settings

import android.os.Bundle
import com.assignment1.hillforts.R
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.AnkoLogger
import views.base.BaseView


class SettingsView : BaseView(), AnkoLogger {

    private lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        presenter = initPresenter(SettingsPresenter(this)) as SettingsPresenter

        settingsLogOutBtn.setOnClickListener {
            presenter.doLogOut()
        }

        editEmail.setOnClickListener {
            presenter.doEditEmail()
        }

        editPassword.setOnClickListener {
            presenter.doEditPassword()
        }
    }
}