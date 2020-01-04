package views.hillforts

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.assignment1.hillforts.helpers.checkLocationPermissions
import com.assignment1.hillforts.helpers.isPermissionGranted
import com.assignment1.hillforts.models.Location
import com.assignment1.hillforts.models.HillfortModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import views.base.BasePresenter
import views.base.BaseView
import views.base.VIEW
import android.text.Html
import com.assignment1.hillforts.R
import org.jetbrains.anko.*


class HillfortsPresenter(view: BaseView) : BasePresenter(view), AnkoLogger{

    private var hillfort = HillfortModel()
    private var edit = false
    private val imageRequest = 1
    private val locationRequest = 2
    private val user = FirebaseAuth.getInstance().currentUser
    private var map: GoogleMap? = null
    private var defaultLocation = Location(52.2461, -7.1387, 15f)
    private var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)

    init {
        if (view.intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = view.intent.extras?.getParcelable("hillfort_edit")!!
            locationUpdate(hillfort.lat, hillfort.lng)
            view.showHillfort(hillfort)
        } else {
            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
        }
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

    fun shareHillfort() {
        val hillfort = hillfort
        if (edit) {
            val sendIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.type = "text/html"
                this.putExtra(
                    Intent.EXTRA_TEXT,
                    Html.fromHtml("<h1>${hillfort.title}</h1><p>Hillfort Title : ${hillfort.title}</p><p>Hillfort Description: ${hillfort.description}</p><p>Hillfort Visited? : ${hillfort.visited}</p><p>Hillfort Additional Notes: ${hillfort.additionalNotes}</p><p>Hillfort Lat Location: ${hillfort.lat}</p><p>Hillfort Lng Location: ${hillfort.lng}</p><p>Hillfort Rating: ${hillfort.rating}")
                )
                this.putExtra(Intent.EXTRA_EMAIL, "danieljcollins17@gmail.com")
            }
            val shareIntent = Intent.createChooser(sendIntent, "Share Hillfort to....")
            view?.startActivity(shareIntent)
        } else {
            view?.toast("Hillfort Details are not complete")
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

    fun doAddOrSave(hillfortTitle: String, hillfortDescription: String, visited: Boolean, hillfortAdditionalNotes: String, hillfortRating: Float) {
        hillfort.title = hillfortTitle
        hillfort.description = hillfortDescription
        hillfort.visited = visited
        hillfort.additionalNotes = hillfortAdditionalNotes
        hillfort.userId = user?.uid.toString()
        hillfort.rating = hillfortRating
        if (hillfort.title.isNotEmpty()) {
            if (!edit) {
                view?.toast(R.string.toast_hillfort_added)
            } else {
                view?.toast(R.string.toast_hillfort_updated)
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
            view?.toast(R.string.toast_empty_fields)
        }
    }

    fun doCancel() {
        if (view?.intent?.hasExtra("hillfort_edit")!!) {
            view?.toast(R.string.toast_hillfort_not_changed)
            view?.finish()
        } else {
            view?.toast(R.string.toast_hillfort_not_added)
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
            view?.toast(R.string.dialog_deleted)
            doAsync {
                app.hillforts.deleteHillfort(hillfort)
                uiThread {
                    view?.finish()
                }
            }
        }
        builder.setNegativeButton(R.string.dialog_cancel) { _, _->
            view?.toast("Cancelled")
        }
        builder.show()
    }

    fun doSelectImage() {
        view?.showImagePickerFunction(view!!, imageRequest)
    }

    fun doSetLocation() {
        view?.navigateTo(VIEW.LOCATION, locationRequest, "location", Location(hillfort.lat, hillfort.lng, hillfort.zoom), "", null)
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == imageRequest) {
            when {
                hillfort.image1 === "" -> {
                    hillfort.image1 = data.data.toString()
                    view?.showHillfort(hillfort)
                    return
                }
                hillfort.image2 === "" -> {
                    hillfort.image2 = data.data.toString()
                    view?.showHillfort(hillfort)
                    return
                }
                hillfort.image3 === "" -> {
                    hillfort.image3 = data.data.toString()
                    view?.showHillfort(hillfort)
                    return
                }
                hillfort.image4 === "" -> {
                    hillfort.image4 = data.data.toString()
                    view?.showHillfort(hillfort)
                    return
                }
            }
        } else if (requestCode == locationRequest) {
            val location = data.extras?.getParcelable<Location>("location")!!
            hillfort.lat = location.lat
            hillfort.lng = location.lng
            hillfort.zoom = location.zoom
            info("@@@ HILLFORT LAT = ${hillfort.lat}")
            info("@@@ HILLFORT LNG = ${hillfort.lng}")
            locationUpdate(hillfort.lat, hillfort.lng)
        }
    }
}