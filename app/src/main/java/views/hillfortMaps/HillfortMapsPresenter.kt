package views.hillfortMaps

import com.assignment1.hillforts.helpers.readImageFromPath
import com.assignment1.hillforts.models.HillfortModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.content_hillfort_maps.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import views.base.BasePresenter
import views.base.BaseView

class HillfortMapsPresenter(view: BaseView): BasePresenter(view) {

    val user = FirebaseAuth.getInstance().currentUser
    private var hillfort = HillfortModel()
    private var allHillforts: List<HillfortModel>? = null

    fun initMap(map: GoogleMap) {
        val defaultLoc = LatLng(52.2461, -7.1387)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLoc, 15.0f))
        doAsync {
            allHillforts = app.hillforts.findAllHillforts().filter { it.userId == user?.uid.toString() }
            uiThread {
                updateMapPage(allHillforts!!, map)
            }
        }
    }

    private fun updateMapPage(hillforts: List<HillfortModel>, map: GoogleMap) {
        hillforts.forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options).tag = it
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
            hillfort = it
            view?.currentTitle!!.text = hillfort.title
            view?.currentDescription!!.text = hillfort.description
            view?.currentImage!!.setImageBitmap(readImageFromPath(view!!, hillfort.image1))
        }
    }

    fun doMarkerClick(marker: Marker): Boolean {
        val hillfort = marker.tag as HillfortModel
        view?.currentTitle!!.text = hillfort.title
        view?.currentDescription!!.text = hillfort.description
        view?.currentImage!!.setImageBitmap(readImageFromPath(view!!, hillfort.image1))
        return false
    }
}