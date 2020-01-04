package views.hillfortList

import com.assignment1.hillforts.R
import com.assignment1.hillforts.models.HillfortModel
import com.assignment1.hillforts.models.Location
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import views.base.BasePresenter
import views.base.BaseView
import views.base.VIEW

class HillfortsListPresenter(view: BaseView): BasePresenter(view), AnkoLogger {

    private val user = FirebaseAuth.getInstance().currentUser
    private var location = Location()

    init {
        if(view.intent.hasExtra("location")) location = view.intent.extras?.getParcelable("location")!!
    }

    fun getHillforts() {
        doAsync {
            val hillforts = app.hillforts.findAllHillforts().filter { it.userId == user?.uid.toString() }
            uiThread {
                view?.showHillforts(hillforts)
            }
        }
    }

    fun doGoToFavs() {
        doAsync {
            val hillforts = app.hillforts.findAllHillforts().filter { it.userId == user?.uid.toString() }
            val favHillforts = hillforts.filter { it.favValue }
            uiThread {
                view?.showHillforts(favHillforts)
            }
        }
    }

    fun doAddHillfort() {
        view?.navigateTo(VIEW.HILLFORT, 0, "user", user, "", null)
    }

    fun doEditHillfort(hillfort: HillfortModel) {
        view?.navigateTo(VIEW.HILLFORT, 0, "user", user, "hillfort_edit", hillfort)
    }

    fun doSettings() {
        view?.navigateTo(VIEW.SETTINGS, 0, "user", user, "", null)
    }

    fun doShowHillfortMap() {
        view?.navigateTo(VIEW.MAPS, 0, "user", user, "", null)
    }
}