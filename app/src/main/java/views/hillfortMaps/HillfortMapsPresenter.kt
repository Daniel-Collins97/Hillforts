package views.hillfortMaps

import com.assignment1.hillforts.helpers.readImageFromPath
import com.assignment1.hillforts.main.MainApp
import com.assignment1.hillforts.models.HillfortModel
import com.assignment1.hillforts.models.UserModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.content_hillfort_maps.*
import views.base.BasePresenter
import views.base.BaseView

class HillfortMapsPresenter(view: BaseView): BasePresenter(view) {

    private var user = UserModel()

    init {
        if (view.intent.hasExtra("user")) {
            user = view.intent.extras?.getParcelable("user")!!
        }
    }

    fun initMap(map: GoogleMap): HillfortModel {
        var hillfort = HillfortModel()
        val defaultLoc = LatLng(52.2461, -7.1387)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLoc, 15.0f))
        app.hillforts.findAll().filter { it.userId == user.id }.forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options).tag = it
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
            hillfort = it
        }
        return hillfort
    }

    fun doMarkerClick(marker: Marker): Boolean {
        val hillfort = marker.tag as HillfortModel
        view?.currentTitle!!.text = hillfort.title
        view?.currentDescription!!.text = hillfort.description
        view?.currentImage!!.setImageBitmap(readImageFromPath(view!!, hillfort.image1))
        return false
    }
}