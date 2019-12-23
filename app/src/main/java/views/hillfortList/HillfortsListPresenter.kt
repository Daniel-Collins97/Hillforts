package views.hillfortList

import com.assignment1.hillforts.models.HillfortModel
import com.assignment1.hillforts.models.Location
import com.assignment1.hillforts.models.UserModel
import views.base.BasePresenter
import views.base.BaseView
import views.base.VIEW

class HillfortsListPresenter(view: BaseView): BasePresenter(view) {

    private var user = UserModel()
    private var location = Location()

    init {
        if(view.intent.hasExtra("location")) location = view.intent.extras?.getParcelable("location")!!
        if (view.intent.hasExtra("user")) user = view.intent.extras?.getParcelable("user")!!
    }

    fun getHillforts(): List<HillfortModel> {
        return app.hillforts.findAll().filter { it.userId == user.id }
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
        view?.navigateTo(VIEW.MAPS, 0, "", null, "", null)
    }
}