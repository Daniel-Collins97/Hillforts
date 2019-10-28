package com.assignment1.hillforts.activities

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.assignment1.hillforts.R
import com.assignment1.hillforts.main.MainApp
import com.assignment1.hillforts.models.HillfortModel
import com.assignment1.hillforts.models.UserModel
import kotlinx.android.synthetic.main.activity_hillforts_list.*
import org.jetbrains.anko.AnkoLogger

class HillfortsListActivity : AppCompatActivity(), HillfortListener, AnkoLogger {

    lateinit var app: MainApp
    private var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillforts_list)
        app = application as MainApp

        if (intent.hasExtra("user")) {
            user = intent.extras?.getParcelable("user")!!
        }


        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        loadHillforts()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = Intent(this@HillfortsListActivity, HillfortActivity::class.java)
        i.putExtra("user", user)
        when (item.itemId) {
            R.id.item_add -> startActivityForResult(i, 0)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onHillfortClick(hillfort: HillfortModel) {
        val i = Intent(this@HillfortsListActivity, HillfortActivity::class.java)
        i.putExtra("user", user)
        i.putExtra("hillfort_edit", hillfort)
        startActivityForResult(i, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadHillforts()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadHillforts() {
        val allHillforts = app.hillforts.findAll().filter { it.userId == user.id }
        showHillforts(allHillforts)
    }

    private fun showHillforts (hillforts: List<HillfortModel>) {
        recyclerView.adapter = HillfortAdapter(hillforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }
}