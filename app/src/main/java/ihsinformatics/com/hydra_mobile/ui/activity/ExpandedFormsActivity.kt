package ihsinformatics.com.hydra_mobile.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ihsinformatics.com.hydra_mobile.R
import kotlinx.android.synthetic.main.activity_expanded_forms.*
import androidx.recyclerview.widget.GridLayoutManager
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.ui.adapter.FormsListDataAdapter
import ihsinformatics.com.hydra_mobile.ui.helper.GridDividerDecoration


class ExpandedFormsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = ""
        setContentView(R.layout.activity_expanded_forms)
        setSupportActionBar(toolbar)
        toolbar_title.text = Constant.componentName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        initView()
    }

    private fun initView() {

        val recyclerView = rv_expanded_gird as RecyclerView
        recyclerView.setHasFixedSize(true)
        val adapter = FormsListDataAdapter(Constant.formList, this)
        val gridLayoutManager = GridLayoutManager(applicationContext, 3)
        recyclerView.addItemDecoration(GridDividerDecoration(this))
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
        super.onBackPressed()

    }
}
