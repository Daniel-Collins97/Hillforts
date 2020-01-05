package views.searchHillforts

import com.assignment1.hillforts.models.HillfortModel
import com.assignment1.hillforts.models.Location
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import views.base.BasePresenter
import views.base.BaseView
import views.base.VIEW

class SearchPresenter(view: BaseView): BasePresenter(view) {

    private val user = FirebaseAuth.getInstance().currentUser
    private var location = Location()
    lateinit var hillforts: List<HillfortModel>

    init {
        if(view.intent.hasExtra("location")) location = view.intent.extras?.getParcelable("location")!!
    }

    fun getHillforts() {
        doAsync {
            hillforts = app.hillforts.findAllHillforts().filter { it.userId == user?.uid.toString() }
            uiThread {
                view?.showHillforts(hillforts)
            }
        }
    }

    fun doEditHillfort(hillfort: HillfortModel) {
        view?.navigateTo(VIEW.HILLFORT, 0, "user", user, "hillfort_edit", hillfort)
    }

    fun checkHillforts(searchParam: CharSequence?) {
        val foundHillforts = ArrayList<HillfortModel>()
        hillforts.forEach {
            if(it.title.contains(searchParam.toString(), ignoreCase = true)) {

                foundHillforts.add(it)
            }
        }
        if(foundHillforts.size > 0) {
            view?.hideProgress()
            view?.showHillforts(foundHillforts)
        } else {
            foundHillforts.clear()
            view?.showHillforts(foundHillforts)
            view?.showProgress()
        }
    }


}