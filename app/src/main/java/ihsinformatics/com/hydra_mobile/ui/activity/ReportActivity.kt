package ihsinformatics.com.hydra_mobile.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import android.widget.ExpandableListView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess
import com.ihsinformatics.dynamicformsgenerator.data.database.history.Encounters
import com.ihsinformatics.dynamicformsgenerator.data.database.history.Ob
import com.ihsinformatics.dynamicformsgenerator.data.pojos.FormEncounterMapper
import com.ihsinformatics.dynamicformsgenerator.data.pojos.History
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.ReportEncountersApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.service.PatientApiService
import ihsinformatics.com.hydra_mobile.ui.adapter.CustomExpandableOfflinePatientAdapter
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
    internal var expandablePatientListView: ExpandableListView? = null

    var adapter: CustomExpandableReportAdapter? = null
    var offlinePatientAdapter: CustomExpandableOfflinePatientAdapter? = null

    private lateinit var networkProgressDialog: NetworkProgressDialog

    var encountersList = ArrayList<Encounters>()
    var titleList: List<String>? = null
    var titlesOfflineEncounters: List<String>? = null

    val data: HashMap<String, List<Ob>>
        get() {

            val listData = HashMap<String, List<Ob>>()


            for (i in encountersList) {
                listData.put(i.encounterType.name, i.obs)
            }

            return listData;
        }

    val offlineData: HashMap<String, List<String>>
        get() {

            var listData = HashMap<String, List<String>>()

            if (patientID != null)
                listData =
                    DataAccess.getInstance().fetchSaveableFormsByPatientIdentifer(this, patientID)

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

        expandableListView = findViewById(R.id.expandableListView)
        expandablePatientListView = findViewById(R.id.expandablePatientListView)


        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val width = metrics.widthPixels

        expandableListView!!.setIndicatorBounds(
            width - GetPixelFromDips(50f),
            width - GetPixelFromDips(10f)
        );
        expandablePatientListView!!.setIndicatorBounds(
            width - GetPixelFromDips(50f),
            width - GetPixelFromDips(10f)
        );


        networkProgressDialog = NetworkProgressDialog(this)
        networkProgressDialog.show()

        encountersList.clear()

        if (isInternetConnected() && !sessionManager.isOfflineMode()) {
            val encounterSearch = RequestManager(
                applicationContext,
                sessionManager.getUsername(),
                sessionManager.getPassword()
            ).getPatientRetrofit().create(PatientApiService::class.java)

            encounterSearch.getEncountersOfPatient(patientID, Constant.REPRESENTATION)
                .enqueue(object : Callback<ReportEncountersApiResponse> {
                    override fun onResponse(
                        call: Call<ReportEncountersApiResponse>,
                        response: Response<ReportEncountersApiResponse>
                    ) {
                        Timber.e(response.message())
                        if (response.isSuccessful) {

                            for (enc in response.body()!!.results) {
                                encountersList.add(enc.encounters)
                            }

                            DataAccess.getInstance().insertOnlineHistory(
                                this@ReportActivity,
                                response.body()!!.results,
                                patientID
                            )


                            networkProgressDialog.dismiss()
                            if (encountersList.size != 0) {
                                setEncounterList()
                            } else {
                                ToastyWidget.getInstance().displayError(
                                    this@ReportActivity,
                                    getString(R.string.no_encounter_for_patient),
                                    Toast.LENGTH_LONG
                                )

                            }
                        }
                    }

                    override fun onFailure(call: Call<ReportEncountersApiResponse>, t: Throwable) {
                        Timber.e(t.localizedMessage)

                        networkProgressDialog.dismiss()
                        Toast.makeText(
                            this@ReportActivity,
                            "Error fetching Online Encounters",
                            Toast.LENGTH_SHORT
                        ).show()

                        fetchOfflineEncounters()
                    }
                })
        } else {

            fetchOfflineEncounters()
            networkProgressDialog.dismiss()
            setEncounterList()
        }
        setEncounterListForOfflinePatient()
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


    fun fetchOfflineEncounters() {
        val serviceHistory =
            DataAccess.getInstance().fetchOnlineHistoryByPatientIdentifier(this, patientID)

        encountersList.addAll(serviceHistory)

    }

    fun setEncounterList() {
        if (expandableListView != null && encountersList != null && encountersList.size != 0) {
            val listData = data
            titleList = ArrayList(listData.keys)
            adapter = CustomExpandableReportAdapter(this, titleList as ArrayList<String>, listData)
            expandableListView!!.setAdapter(adapter)

        }
    }

    fun setEncounterListForOfflinePatient() {
        if (expandablePatientListView != null) {
            val offlineForms = offlineData
            titlesOfflineEncounters = ArrayList(offlineForms.keys)
            offlinePatientAdapter = CustomExpandableOfflinePatientAdapter(
                this,
                titlesOfflineEncounters as ArrayList<String>, offlineForms
            )
            expandablePatientListView!!.setAdapter(offlinePatientAdapter)

        }

    }

    fun GetPixelFromDips(pixels: Float): Int {
        // Get the screen's density scale
        val scale = resources.displayMetrics.density
        // Convert the dps to pixels, based on density scale
        return (pixels * scale + 0.5f).toInt()
    }


}
