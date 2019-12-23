package views.editLocation

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.assignment1.hillforts.models.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.AnkoLogger
import views.base.BasePresenter
import views.base.BaseView

class EditLocationPresenter(view: BaseView): BasePresenter(view), AnkoLogger {

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

    fun doMarkerDragEnd(lat: Double, lng: Double) {
        location.lat = lat
        location.lng = lng
    }
}