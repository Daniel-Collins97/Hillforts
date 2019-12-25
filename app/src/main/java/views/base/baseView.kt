package views.base

import android.content.Intent

import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.AnkoLogger

import com.assignment1.hillforts.models.HillfortModel
import views.editLocation.EditLocationView
import views.hillfortMaps.HillfortMapsView
import views.hillforts.HillfortsView
import views.hillfortList.HillfortsListView
import views.login.LoginView
import views.settings.SettingsView
import views.signup.SignupView

enum class VIEW {
    LOCATION, HILLFORT, MAPS, LIST, SETTINGS, LOGIN, SIGNUP
}

abstract class BaseView : AppCompatActivity(), AnkoLogger {

    private var basePresenter: BasePresenter? = null

    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null, key2: String, value2: Parcelable? = null) {
        var intent = Intent(this, HillfortsListView::class.java)
        when (view) {
            VIEW.LOCATION -> intent = Intent(this, EditLocationView::class.java)
            VIEW.HILLFORT -> intent = Intent(this, HillfortsView::class.java)
            VIEW.MAPS -> intent = Intent(this, HillfortMapsView::class.java)
            VIEW.LIST -> intent = Intent(this, HillfortsListView::class.java)
            VIEW.SETTINGS -> intent = Intent(this, SettingsView::class.java)
            VIEW.LOGIN -> intent = Intent(this, LoginView::class.java)
            VIEW.SIGNUP -> intent = Intent(this, SignupView::class.java)
        }
        if (key != "") {
            intent.putExtra(key, value)
        }
        if (key2 != "") {
            intent.putExtra(key2, value2)
        }
        startActivityForResult(intent, code)
    }

    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    override fun onDestroy() {
        basePresenter?.onDestroy()
        super.onDestroy()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            basePresenter?.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    open fun showHillfort(hillfort: HillfortModel) {}
    open fun showHillforts(hillforts: List<HillfortModel>) {}
    open fun showProgress() {}
    open fun hideProgress() {}
}