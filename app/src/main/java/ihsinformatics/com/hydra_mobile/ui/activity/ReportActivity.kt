package ihsinformatics.com.hydra_mobile.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import android.widget.ExpandableListView
import android.widget.Toast
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.ReportEncountersApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.Reports.Encounters
import ihsinformatics.com.hydra_mobile.data.remote.model.Reports.Ob
import ihsinformatics.com.hydra_mobile.data.remote.service.CommonLabApiService
import ihsinformatics.com.hydra_mobile.ui.adapter.CustomExpandableReportAdapter
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import ihsinformatics.com.hydra_mobile.ui.dialogs.NetworkProgressDialog
import kotlinx.android.synthetic.main.activity_report.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class ReportActivity : BaseActivity() {

    var patientID = Global.patientData.patient.identifier


    internal var expandableListView: ExpandableListView? = null
    var adapter: CustomExpandableReportAdapter? = null

    private lateinit var networkProgressDialog: NetworkProgressDialog

    var encountersList = ArrayList<Encounters>()
    var titleList: List<String>? = null

    val data: HashMap<String, List<Ob>>
        get() {

            val listData = HashMap<String, List<Ob>>()


            for (i in encountersList) {
              listData.put(i.encounterType.name,i.obs)
            }

            return listData;
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = ""
        setContentView(R.layout.activity_report)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        initView()
    }

    private fun initView() {

        expandableListView = findViewById<ExpandableListView>(R.id.expandableListView)


        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val width = metrics.widthPixels

        expandableListView!!.setIndicatorBounds(width - GetPixelFromDips(50f), width - GetPixelFromDips(10f));





        networkProgressDialog = NetworkProgressDialog(this)
        networkProgressDialog.show()

        val testOrderSearch = RequestManager(applicationContext, sessionManager.getUsername(), sessionManager.getPassword()).getPatientRetrofit().create(CommonLabApiService::class.java)

        testOrderSearch.getEncountersOfPatient(patientID, Constant.REPRESENTATION).enqueue(object : Callback<ReportEncountersApiResponse> {
            override fun onResponse(
                call: Call<ReportEncountersApiResponse>, response: Response<ReportEncountersApiResponse>
                                   ) {
                Timber.e(response.message())
                if (response.isSuccessful) {
                    if (response.isSuccessful) {
                        encountersList.clear()
                        encountersList = response.body()!!.encounters

                        if (encountersList.size != 0) {

                            networkProgressDialog.dismiss()
                            setEncounterList()

                        } else {
                            networkProgressDialog.dismiss()

                            ToastyWidget.getInstance().displayError(this@ReportActivity,getString(R.string.no_encounter_for_patient),Toast.LENGTH_LONG)
                            startActivity(Intent(this@ReportActivity,HomeActivity::class.java))

                        }

                    }
                }
            }

            override fun onFailure(call: Call<ReportEncountersApiResponse>, t: Throwable) {
                Timber.e(t.localizedMessage)

                networkProgressDialog.dismiss()

                Toast.makeText(this@ReportActivity, "Error fetching Encounters", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@ReportActivity,HomeActivity::class.java))
            }
        })

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


    fun setEncounterList() {

        if (expandableListView != null) {
            val listData = data
            titleList = ArrayList(listData.keys)
            adapter = CustomExpandableReportAdapter(this, titleList as ArrayList<String>, listData)
            expandableListView!!.setAdapter(adapter)


        }

    }


    fun GetPixelFromDips(pixels: Float): Int {
        // Get the screen's density scale
        val scale = resources.displayMetrics.density
        // Convert the dps to pixels, based on density scale
        return (pixels * scale + 0.5f).toInt()
    }

}
