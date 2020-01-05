package views.searchHillforts

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.assignment1.hillforts.R
import com.assignment1.hillforts.activities.HillfortAdapter
import com.assignment1.hillforts.activities.HillfortListener
import com.assignment1.hillforts.models.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_hillforts_list.recyclerView
import kotlinx.android.synthetic.main.activity_search_hillforts.*
import views.base.BaseView
import views.base.VIEW


class SearchView : BaseView(), HillfortListener {
    val user = FirebaseAuth.getInstance().currentUser
    private lateinit var presenter: SearchPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_hillforts)
        presenter = initPresenter(SearchPresenter(this)) as SearchPresenter

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        presenter.getHillforts()
        noFoundHillforts.visibility = View.GONE

        searchParam.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(searchParam: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.checkHillforts(searchParam)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_back -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showProgress() {
        noFoundHillforts.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        noFoundHillforts.visibility = View.GONE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        navigateTo(VIEW.LIST, 0, "user", user, "", null)
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