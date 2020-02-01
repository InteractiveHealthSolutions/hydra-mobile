package ihsinformatics.com.hydra_mobile.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.CommonLabApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.EncountersApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.Encounter
import ihsinformatics.com.hydra_mobile.data.remote.service.CommonLabApiService
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.TestAdder
import ihsinformatics.com.hydra_mobile.ui.adapter.CommonLabAdapter
import ihsinformatics.com.hydra_mobile.ui.adapter.ReportAdapter
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import ihsinformatics.com.hydra_mobile.ui.dialogs.NetworkProgressDialog
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import kotlinx.android.synthetic.main.activity_report.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class ReportActivity : BaseActivity() {

    //TODO Hardcoded patient must be fetch from api     ~Taha PatientIssue
    var patientID = Global.patientData.patient.identifier

    private lateinit var networkProgressDialog: NetworkProgressDialog

    var encountersList = ArrayList<Encounter>()

    lateinit var adapter: ReportAdapter
    lateinit var rv: RecyclerView

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

        rv = findViewById<RecyclerView>(R.id.encountersList)
        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        networkProgressDialog = NetworkProgressDialog(this)
        networkProgressDialog.show()

        val testOrderSearch = RequestManager(applicationContext, sessionManager.getUsername(), sessionManager.getPassword()).getPatientRetrofit().create(CommonLabApiService::class.java)

        testOrderSearch.getEncountersOfPatient(patientID, Constant.REPRESENTATION).enqueue(object : Callback<EncountersApiResponse> {
            override fun onResponse(
                call: Call<EncountersApiResponse>, response: Response<EncountersApiResponse>
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
                            networkProgressDialog.dismiss()

                        }

                    }
                }
            }

            override fun onFailure(call: Call<EncountersApiResponse>, t: Throwable) {
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

        adapter = ReportAdapter(encountersList, this)
        rv.adapter = adapter
    }

}
