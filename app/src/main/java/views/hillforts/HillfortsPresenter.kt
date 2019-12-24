package views.hillforts

import android.annotation.SuppressLint
import android.content.Intent
import org.jetbrains.anko.uiThread
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.assignment1.hillforts.R
import com.assignment1.hillforts.helpers.checkLocationPermissions
import com.assignment1.hillforts.helpers.isPermissionGranted
import com.assignment1.hillforts.models.Location
import com.assignment1.hillforts.helpers.showImagePicker
import com.assignment1.hillforts.models.HillfortModel
import com.assignment1.hillforts.models.UserModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import views.base.BasePresenter
import views.base.BaseView
import views.base.VIEW

class HillfortsPresenter(view: BaseView) : BasePresenter(view), AnkoLogger{

    private var hillfort = HillfortModel()
    private var edit = false
    private val imageRequest = 1
    private val locationRequest = 2
    private var user = UserModel()
    private var map: GoogleMap? = null
    private var defaultLocation = Location(52.2461, -7.1387, 15f)
    private var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)

    init {
        if (view.intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = view.intent.extras?.getParcelable("hillfort_edit")!!
            view.showHillfort(hillfort)
        } else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
        }
        if (view.intent.hasExtra("user")) user = view.intent.extras?.getParcelable("user")!!
    }

    fun doConfigMap(m: GoogleMap) {
        map = m
        locationUpdate(hillfort.lat, hillfort.lng)
    }

    override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if(isPermissionGranted(requestCode, grantResults)) {
            doSetCurrentLocation()
        } else {
            locationUpdate(defaultLocation.lat, defaultLocation.lng)
        }
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    private fun locationUpdate(lat: Double, lng: Double) {
        hillfort.lat = lat
        hillfort.lng = lng
        hillfort.zoom = 15f
        map?.clear()
        map?.uiSettings?.isZoomControlsEnabled = true
        val options = MarkerOptions().title(hillfort.title).position(LatLng(hillfort.lat, hillfort.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(hillfort.lat, hillfort.lng), hillfort.zoom))
        view?.showHillfort(hillfort)
    }

    fun doAddOrSave(hillfortTitle: String, hillfortDescription: String, visited: Boolean, hillfortAdditionalNotes: String) {
        hillfort.title = hillfortTitle
        hillfort.description = hillfortDescription
        hillfort.visited = visited
        hillfort.additionalNotes = hillfortAdditionalNotes
        hillfort.userId = user.id
        if (hillfort.title.isNotEmpty()) {
            if (!edit) {
                Toast.makeText(view, R.string.toast_hillfort_added, Toast.LENGTH_LONG).show()
            }
            doAsync {
                if (edit) {
                    app.hillforts.updateHillfort(hillfort.copy())
                } else {
                    app.hillforts.createHillfort(hillfort.copy())
                }
                uiThread {
                    view?.finish()
                }
            }
        }
        else {
            Toast.makeText(view, R.string.toast_empty_fields, Toast.LENGTH_LONG).show()
        }
    }

    fun doCancel() {
        if (view?.intent?.hasExtra("hillfort_edit")!!) {
            Toast.makeText(view, R.string.toast_hillfort_not_changed, Toast.LENGTH_LONG).show()
            view?.finish()
        } else {
            Toast.makeText(view, R.string.toast_hillfort_not_added, Toast.LENGTH_LONG).show()
            view?.finish()
        }
    }

    fun doDelete() {
        showAlertDialogButtonClicked()
    }

    private fun showAlertDialogButtonClicked() {
        val builder = AlertDialog.Builder(view!!)
        builder.setTitle(R.string.dialog_delete_title)
        builder.setMessage(R.string.dialog_delete_confirmation)
        builder.setPositiveButton(R.string.dialog_confirm) { _, _ ->
            Toast.makeText(view, R.string.dialog_deleted, Toast.LENGTH_LONG).show()
            doAsync {
                app.hillforts.deleteHillfort(hillfort)
                uiThread {
                    view?.finish()
                }
            }
        }
        builder.setNegativeButton(R.string.dialog_cancel) { _, _->
            Toast.makeText(view, "Cancelled", Toast.LENGTH_LONG).show()
        }
        builder.show()
    }

    fun doSelectImage() {
        view?.let {
            showImagePicker(view!!, imageRequest)
        }
    }

    fun doSetLocation() {
        view?.navigateTo(VIEW.LOCATION, locationRequest, "location", Location(hillfort.lat, hillfort.lng, hillfort.zoom), "", null)
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            imageRequest -> {
                when {
                    hillfort.image1 == "" -> {
                        hillfort.image1 = data.data.toString()
                        view?.showHillfort(hillfort)
                    }
                    hillfort.image2 == "" -> {
                        hillfort.image2 = data.data.toString()
                        view?.showHillfort(hillfort)
                    }
                    hillfort.image3 == "" -> {
                        hillfort.image3 = data.data.toString()
                        view?.showHillfort(hillfort)
                    }
                    hillfort.image4 == "" -> {
                        hillfort.image4 = data.data.toString()
                        view?.showHillfort(hillfort)
                    }
                }
            }

            locationRequest -> {
                val location = data.extras?.getParcelable<Location>("location")!!
                hillfort.lat = location.lat
                hillfort.lng = location.lng
                hillfort.zoom = location.zoom
                locationUpdate(hillfort.lat, hillfort.lng)
            }
        }
    }
}