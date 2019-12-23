package views.hillfortMaps

import android.os.Bundle
import com.assignment1.hillforts.R
import com.assignment1.hillforts.helpers.readImageFromPath
import com.assignment1.hillforts.main.MainApp
import com.assignment1.hillforts.models.HillfortModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.content_hillfort_maps.*

import org.jetbrains.anko.AnkoLogger
import views.base.BaseView

class HillfortMapsView : BaseView(), AnkoLogger, GoogleMap.OnMarkerClickListener{

    lateinit var app: MainApp
    lateinit var map: GoogleMap
    lateinit var presenter: HillfortMapsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_maps)
        app = application as MainApp
        hillfortMapView.onCreate(savedInstanceState)
        presenter = initPresenter(HillfortMapsPresenter(this)) as HillfortMapsPresenter

        hillfortMapView.getMapAsync {
            map = it
            map.uiSettings.isZoomControlsEnabled = true
            map.setOnMarkerClickListener(this)
            val currentHillfort = presenter.initMap(map)
            currentTitle.text = currentHillfort.title
            currentDescription!!.text = currentHillfort.description
            currentImage!!.setImageBitmap(readImageFromPath(this, currentHillfort.image1))
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        return presenter.doMarkerClick(marker)
    }

    override fun onDestroy() {
        super.onDestroy()
        hillfortMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        hillfortMapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        hillfortMapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        hillfortMapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        hillfortMapView.onSaveInstanceState(outState)
    }
}
