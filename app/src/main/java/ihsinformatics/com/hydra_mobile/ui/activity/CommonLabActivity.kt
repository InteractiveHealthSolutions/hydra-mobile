package ihsinformatics.com.hydra_mobile.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.CommonLabModel
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_common_lab.*

class CommonLabActivity : BaseActivity() {

    val TAG = "Test Type"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = ""
        setContentView(R.layout.activity_common_lab)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val rv = findViewById<RecyclerView>(R.id.recyclerView)
        rv.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        val testTypeList =ArrayList<CommonLabModel>()

        testTypeList.add(CommonLabModel(TAG,"RNA Viral Load Test"))
        testTypeList.add(CommonLabModel(TAG,"RNA Viral Load Test"))
        testTypeList.add(CommonLabModel(TAG,"CBC"))
        testTypeList.add(CommonLabModel(TAG,"AFB Culture"))
        testTypeList.add(CommonLabModel(TAG,"AFB Culture"))
        testTypeList.add(CommonLabModel(TAG,"AFB Culture"))
        testTypeList.add(CommonLabModel(TAG,"AFB Culture"))
        testTypeList.add(CommonLabModel(TAG,"AFB Culture"))
        testTypeList.add(CommonLabModel(TAG,"Hemoglobin Result Value"))
        testTypeList.add(CommonLabModel(TAG,"AFB Culture"))
        testTypeList.add(CommonLabModel(TAG,"AFB Culture"))
        testTypeList.add(CommonLabModel(TAG,"AFB Culture"))
        testTypeList.add(CommonLabModel(TAG,"AFB Culture"))
        testTypeList.add(CommonLabModel(TAG,"AFB Culture"))
        testTypeList.add(CommonLabModel(TAG,"AFB Culture"))
        testTypeList.add(CommonLabModel(TAG,"AFB Culture"))


        var adapter = CustomRecycleViewAdapter(testTypeList)
        rv.adapter = adapter


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