package com.assignment1.hillforts.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import com.assignment1.hillforts.R
import com.assignment1.hillforts.helpers.readImage
import com.assignment1.hillforts.helpers.readImageFromPath
import com.assignment1.hillforts.helpers.showImagePicker
import com.assignment1.hillforts.main.MainApp
import com.assignment1.hillforts.models.HillfortModel
import com.assignment1.hillforts.models.Location
import com.assignment1.hillforts.models.UserModel
import kotlinx.android.synthetic.main.activity_hillfort.hillfortTitle
import org.jetbrains.anko.intentFor


class HillfortActivity : AppCompatActivity(), AnkoLogger {

    private var hillfort = HillfortModel()
    val hillforts= ArrayList<HillfortModel>()
    lateinit var app : MainApp
    private var edit = false
    private val imageRequest = 1
    private val locationRequest = 2
    private var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        app = application as MainApp

        if (intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = intent.extras?.getParcelable("hillfort_edit")!!
            hillfortTitle.setText(hillfort.title)
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
            }
            hillfortDescription.setText(hillfort.description)
            hillfortAdditionalNotes.setText(hillfort.additionalNotes)
            visited.isChecked = hillfort.visited
            chooseImage.setText(R.string.change_hillfort_image)
            btnAdd.setText(R.string.save_hillfort)
        }

        if (intent.hasExtra("user")) {
            user = intent.extras?.getParcelable("user")!!
        }

        btnAdd.setOnClickListener {
            hillfort.title = hillfortTitle.text.toString()
            hillfort.description = hillfortDescription.text.toString()
            hillfort.visited = visited.isChecked
            hillfort.additionalNotes = hillfortAdditionalNotes.text.toString()
            hillfort.userId = user.id
            if (hillfort.title.isNotEmpty()) {
                if (edit) {
                    app.hillforts.update(hillfort.copy())
                } else {
                    app.hillforts.create(hillfort.copy())
                    toast(R.string.toast_hillfort_added)
                }
                setResult(RESULT_OK)
                finish()
            }
            else {
                toast(R.string.toast_empty_fields)
            }
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
            showImagePicker(this, imageRequest)
        }

        hillfortLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (hillfort.zoom != 0f) {
                location.lat = hillfort.lat
                location.lng = hillfort.lng
                location.zoom = hillfort.zoom
            }
            startActivityForResult(intentFor<MapActivity>().putExtra("location", location), locationRequest)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                if (intent.hasExtra("hillfort_edit")) {
                    toast(R.string.toast_hillfort_not_changed)
                    finish()
                } else {
                    toast(R.string.toast_hillfort_not_added)
                    finish()
                }
            }
            R.id.item_delete -> {
                showAlertDialogButtonClicked()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun showAlertDialogButtonClicked() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.dialog_delete_title)
        builder.setMessage(R.string.dialog_delete_confirmation)
        builder.setPositiveButton(R.string.dialog_confirm) { _, _ ->
            toast(R.string.dialog_deleted)
            app.hillforts.delete(hillfort)
            finish()
        }
        builder.setNegativeButton(R.string.dialog_cancel) { _, _->
            toast("Cancelled")
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            imageRequest -> {
                if (data != null) {
                    when {
                        hillfort.image1 == "" -> {
                            hillfort.image1 = data.data.toString()
                            hillfortImage1.setImageBitmap(readImage(this, resultCode, data))
                        }
                        hillfort.image2 == "" -> {
                            hillfort.image2 = data.data.toString()
                            hillfortImage2.setImageBitmap(readImage(this, resultCode, data))
                        }
                        hillfort.image3 == "" -> {
                            hillfort.image3 = data.data.toString()
                            hillfortImage3.setImageBitmap(readImage(this, resultCode, data))
                        }
                        hillfort.image4 == "" -> {
                            hillfort.image4 = data.data.toString()
                            hillfortImage4.setImageBitmap(readImage(this, resultCode, data))
                            chooseImage.setText(R.string.change_hillfort_image)
                        }
                    }
                }
            }

            locationRequest -> {
                if (data != null) {
                    val location = data.extras?.getParcelable<Location>("location")!!
                    hillfort.lat = location.lat
                    hillfort.lng = location.lng
                    hillfort.zoom = location.zoom
                    hillfortLocation.setText(R.string.change_hillfort_location)
                }
            }
        }
    }
}
