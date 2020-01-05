package views.editLocation

import android.content.Intent
import com.assignment1.hillforts.models.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.AnkoLogger
import views.base.BasePresenter
import views.base.BaseView

class EditLocationPresenter(view: BaseView): BasePresenter(view) {

    private lateinit var map: GoogleMap
    private var location = Location()

    init {
        if (view.intent.hasExtra("location")) location = view.intent.getParcelableExtra("location")!!
    }

    fun doMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        val loc = LatLng(location.lat, location.lng)
        val options = MarkerOptions()
            .title("Hillfort")
            .snippet("GPS : $loc")
            .draggable(true)
            .position(loc)
        map.addMarker(options)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
    }

    fun doOnMarkerClick(marker: Marker): Boolean {
        val loc = LatLng(marker.position.latitude, marker.position.longitude)
        marker.snippet = "GPS : $loc"
        return false
    }

    fun doOnBackPressed() {
        val resultIntent = Intent()
        resultIntent.putExtra("location", location)
        view?.setResult(0, resultIntent)
        view?.finish()
    }

    fun doMarkerDragEnd(marker: Marker?) {
        location.lat = marker?.position!!.latitude
        location.lng = marker.position.longitude
    }
}