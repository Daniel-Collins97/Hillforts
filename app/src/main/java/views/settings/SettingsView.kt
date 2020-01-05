package views.settings

import android.os.Bundle
import android.view.View
import com.assignment1.hillforts.R
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_settings.progressBar
import views.base.BaseView


class SettingsView : BaseView() {

    private lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        presenter = initPresenter(SettingsPresenter(this)) as SettingsPresenter
        progressBar.visibility = View.GONE

        settingsLogOutBtn.setOnClickListener {
            presenter.doLogOut()
        }

        editEmail.setOnClickListener {
            presenter.doEditEmail()
        }

        settingsChangePasswordBtn.setOnClickListener {
            presenter.doEditPassword()
        }
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }
}