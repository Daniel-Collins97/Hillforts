package views.editLocation

import android.os.Bundle
import com.assignment1.hillforts.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import views.base.BaseView

class EditLocationView : BaseView(), OnMapReadyCallback,  GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener  {

    override fun onMarkerDragStart(marker: Marker) {
    }

    override fun onMarkerDrag(marker: Marker) {
    }

    override fun onMarkerDragEnd(marker: Marker) {
        presenter.doMarkerDragEnd(marker)
    }

    private lateinit var presenter: EditLocationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        presenter = initPresenter(EditLocationPresenter(this)) as EditLocationPresenter
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setOnMarkerDragListener(this)
        googleMap.setOnMarkerClickListener(this)
        presenter.doMapReady(googleMap)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        return presenter.doOnMarkerClick(marker)
    }

    override fun onBackPressed() {
        presenter.doOnBackPressed()
    }
}