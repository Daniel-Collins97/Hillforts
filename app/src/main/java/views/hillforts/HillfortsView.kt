package views.hillforts

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import com.assignment1.hillforts.R
import com.assignment1.hillforts.helpers.readImageFromPath
import com.assignment1.hillforts.models.HillfortModel
import kotlinx.android.synthetic.main.activity_hillfort.hillfortTitle
import views.base.BaseView


class HillfortsView : BaseView(), AnkoLogger {

    private lateinit var presenter: HillfortsPresenter
    private var hillfort = HillfortModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        presenter = initPresenter(HillfortsPresenter(this)) as HillfortsPresenter
        hillfortMapLoc.onCreate(savedInstanceState)
        hillfortMapLoc.getMapAsync {
            presenter.doConfigMap(it)
        }

        btnAdd.setOnClickListener {
            presenter.doAddOrSave(hillfortTitle.text.toString(), hillfortDescription.text.toString(), visited.isChecked, hillfortAdditionalNotes.text.toString())
        }

        clearAll.setOnClickListener {
            hillfortImage1.setImageResource(R.drawable.logo)
            hillfort.image1 = ""
            hillfortImage2.setImageResource(R.drawable.logo)
            hillfort.image2 = ""
            hillfortImage3.setImageResource(R.drawable.logo)
            hillfort.image3 = ""
            hillfortImage4.setImageResource(R.drawable.logo)
            hillfort.image4 = ""
        }

        chooseImage.setOnClickListener {
            presenter.doSelectImage()
            if(hillfort.image4 != "") {
                chooseImage.setText(R.string.change_hillfort_image)
            }
        }

        hillfortLocation.setOnClickListener {
            presenter.doSetLocation()
            hillfortLocation.setText(R.string.change_hillfort_location)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        hillfortMapLoc.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        hillfortMapLoc.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        hillfortMapLoc.onPause()
    }

    override fun onResume() {
        super.onResume()
        hillfortMapLoc.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        hillfortMapLoc.onSaveInstanceState(outState)
    }

    override fun showHillfort(hillfort: HillfortModel) {
        hillfortTitle.setText(hillfort.title)
        hillfortDescription.setText(hillfort.description)
        if (hillfort.image1 != "") {
            hillfortImage1.setImageBitmap(readImageFromPath(this, hillfort.image1))
        }
        if (hillfort.image2 != "") {
            hillfortImage2.setImageBitmap(readImageFromPath(this, hillfort.image2))
        }
        if (hillfort.image3 != "") {
            hillfortImage3.setImageBitmap(readImageFromPath(this, hillfort.image3))
        }
        if (hillfort.image4 != "") {
            hillfortImage4.setImageBitmap(readImageFromPath(this, hillfort.image4))
            chooseImage.setText(R.string.change_hillfort_image)
        }
        hillfortAdditionalNotes.setText(hillfort.additionalNotes)
        visited.isChecked = hillfort.visited
        btnAdd.setText(R.string.save_hillfort)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                presenter.doCancel()
            }
            R.id.item_delete -> {
                presenter.doDelete()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            presenter.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        presenter.doCancel()
    }
}
