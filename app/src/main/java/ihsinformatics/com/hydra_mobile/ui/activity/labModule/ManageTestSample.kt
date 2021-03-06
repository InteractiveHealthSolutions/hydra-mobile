package ihsinformatics.com.hydra_mobile.ui.activity.labModule

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import android.widget.Button
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.TestSample
import ihsinformatics.com.hydra_mobile.data.remote.service.CommonLabApiService
import ihsinformatics.com.hydra_mobile.ui.adapter.CustomExpandableManageTestSampleAdapter
import ihsinformatics.com.hydra_mobile.utils.SessionManager

class ManageTestSample : AppCompatActivity() {

    internal var expandableListView: ExpandableListView? = null
    internal var adapter: ExpandableListAdapter? = null
    internal var titleList: List<String>? = null

    lateinit var labTest: String

    val data: LinkedHashMap<String, ArrayList<TestSample>>
        get() {

            val listData = LinkedHashMap<String, ArrayList<TestSample>>()


            val intent = intent
            val jsonObj = intent.getStringExtra("testSamples")

            if (null != jsonObj) {
                val gson = Gson()
                val token: TypeToken<ArrayList<TestSample?>?> = object : TypeToken<ArrayList<TestSample?>?>() {}
                var manageTestSamplesList: ArrayList<TestSample> = gson.fromJson(jsonObj, token.type)

                listData.put("Test Samples", manageTestSamplesList)
            }

            return listData
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_test_sample)

        var addTestSample = findViewById<Button>(R.id.addTestSample)
        expandableListView = findViewById(ihsinformatics.com.hydra_mobile.R.id.expandableListView)

        val intentFrom = intent
        labTest = intentFrom.getStringExtra("labTest")

        addTestSample.setOnClickListener {

            var intent = Intent(this@ManageTestSample, TestSampleAdder::class.java)
            intent.putExtra("labTest", labTest)
            startActivity(intent)
        }

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val width = metrics.widthPixels

        expandableListView!!.setIndicatorBounds(width - GetPixelFromDips(50f), width - GetPixelFromDips(10f));

        if (expandableListView != null) {
            val sessionManager = SessionManager(this)
            val testSampleConcepts = RequestManager(applicationContext, sessionManager.getUsername(), sessionManager.getPassword()).getPatientRetrofit().create(CommonLabApiService::class.java)

            val listData = data
            titleList = ArrayList(listData.keys)
            adapter = CustomExpandableManageTestSampleAdapter(this, titleList as ArrayList<String>, listData,testSampleConcepts)
            expandableListView!!.setAdapter(adapter)
        }


    }


    fun GetPixelFromDips(pixels: Float): Int {
        // Get the screen's density scale
        val scale = resources.displayMetrics.density
        // Convert the dps to pixels, based on density scale
        return (pixels * scale + 0.5f).toInt()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, CommonLabActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
