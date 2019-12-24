package views.hillfortList

import com.assignment1.hillforts.models.HillfortModel
import com.assignment1.hillforts.models.Location
import com.assignment1.hillforts.models.UserModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import views.base.BasePresenter
import views.base.BaseView
import views.base.VIEW

class HillfortsListPresenter(view: BaseView): BasePresenter(view), AnkoLogger {

    private var user = UserModel()
    private var location = Location()

    init {
        if(view.intent.hasExtra("location")) location = view.intent.extras?.getParcelable("location")!!
        if (view.intent.hasExtra("user")) user = view.intent.extras?.getParcelable("user")!!
    }

    fun getHillforts() {
        doAsync {
            val hillforts = app.hillforts.findAllHillforts().filter { it.userId == user.id }
            uiThread {
                info("@@@ LOGGED IN USER = ${user.id}")
                hillforts.forEach {
                    info("@@@ USERID = ${it.userId}")
                }
                view?.showHillforts(hillforts)
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