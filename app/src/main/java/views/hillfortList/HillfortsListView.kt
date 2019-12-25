package views.hillfortList

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.assignment1.hillforts.R
import com.assignment1.hillforts.activities.HillfortAdapter
import com.assignment1.hillforts.activities.HillfortListener
import views.hillforts.HillfortsView
import views.login.LoginView
import com.assignment1.hillforts.models.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_hillforts_list.*
import views.base.BaseView

class HillfortsListView : BaseView(),  HillfortListener{

    val user = FirebaseAuth.getInstance().currentUser
    private lateinit var presenter: HillfortsListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillforts_list)
        presenter = initPresenter(HillfortsListPresenter(this)) as HillfortsListPresenter

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.getHillforts()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = Intent(this@HillfortsListView, HillfortsView::class.java)
        i.putExtra("user", user)
        when (item.itemId) {
            R.id.item_add -> presenter.doAddHillfort()
            R.id.item_settings -> presenter.doSettings()
            R.id.item_map -> presenter.doShowHillfortMap()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@HillfortsListView, LoginView::class.java)
        startActivity(intent)
    }

    override fun onHillfortClick(hillfort: HillfortModel) {
        presenter.doEditHillfort(hillfort)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.getHillforts()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun showHillforts (hillforts: List<HillfortModel>) {
        recyclerView.adapter = HillfortAdapter(hillforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }
}