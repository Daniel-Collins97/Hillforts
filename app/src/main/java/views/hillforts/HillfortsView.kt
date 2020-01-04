package views.hillforts

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RatingBar
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import com.assignment1.hillforts.R
import com.assignment1.hillforts.models.HillfortModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_hillfort.hillfortTitle
import kotlinx.android.synthetic.main.activity_settings.view.*
import kotlinx.android.synthetic.main.card_hillfort.*
import org.jetbrains.anko.info
import views.base.BaseView




class HillfortsView : BaseView(), AnkoLogger, RatingBar.OnRatingBarChangeListener {

    private lateinit var presenter: HillfortsPresenter
    private var hillfort = HillfortModel()
    var hillfortTitleStr: String = ""
    var hillfortDescStr: String = ""
    var hillfortVisitedStr: Boolean = false
    var hillfortAdditionalNotesStr: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        presenter = initPresenter(HillfortsPresenter(this)) as HillfortsPresenter
        hillfortMapLoc.onCreate(savedInstanceState)
        hillfortMapLoc.getMapAsync {
            presenter.doConfigMap(it)
        }
        ratingBar1.onRatingBarChangeListener = this

        btnAdd.setOnClickListener {
            presenter.doAddOrSave(hillfortTitle.text.toString(), hillfortDescription.text.toString(), visited.isChecked, hillfortAdditionalNotes.text.toString(), ratingBar1.rating)
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

        shareButton.setOnClickListener {
            presenter.shareHillfort()
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

    override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
        hillfort.rating = p1
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
        hillfortTitleStr = hillfortTitle.text.toString()
        hillfortDescStr = hillfortDescription.text.toString()
        hillfortVisitedStr = visited.isChecked
        hillfortAdditionalNotesStr = hillfortAdditionalNotes.text.toString()
        hillfortMapLoc.onPause()
    }

    override fun onResume() {
        super.onResume()
        hillfortTitle.setText(hillfortTitleStr)
        hillfortDescription.setText(hillfortDescStr)
        visited.setChecked(hillfortVisitedStr)
        hillfortAdditionalNotes.setText(hillfortAdditionalNotesStr)
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
            Glide.with(this).load(hillfort.image1).into(hillfortImage1)
            info("IMAGE1HERE")
        }
        if (hillfort.image2 != "") {
            Glide.with(this).load(hillfort.image2).into(hillfortImage2)
            info("IMAGE2HERE")
        }
        if (hillfort.image3 != "") {
            Glide.with(this).load(hillfort.image3).into(hillfortImage3)
            info("IMAGE3HERE")
        }
        if (hillfort.image4 != "") {
            Glide.with(this).load(hillfort.image4).into(hillfortImage4)
            info("IMAGE4HERE")
            chooseImage.setText(R.string.change_hillfort_image)
        }
        hillfortAdditionalNotes.setText(hillfort.additionalNotes)
        visited.isChecked = hillfort.visited
        ratingBar1.rating = hillfort.rating
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
