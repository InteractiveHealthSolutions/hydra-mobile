package ihsinformatics.com.hydra_mobile.ui.activity.labModule

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestOrder
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.CommonLabApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.Encounter
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.EncountersApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.service.CommonLabApiService
import ihsinformatics.com.hydra_mobile.ui.activity.HomeActivity
import ihsinformatics.com.hydra_mobile.ui.adapter.CommonLabAdapter
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import kotlinx.android.synthetic.main.activity_common_lab.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import ihsinformatics.com.hydra_mobile.R
import android.app.ProgressDialog
import ihsinformatics.com.hydra_mobile.ui.dialogs.NetworkProgressDialog


class CommonLabActivity : AppCompatActivity() {


    lateinit var testTypeList: ArrayList<LabTestOrder>
    lateinit var encountersList: ArrayList<Encounter>
    lateinit var adapter: CommonLabAdapter
    lateinit var rv: RecyclerView
    lateinit var sessionManager: SessionManager

    private lateinit var networkProgressDialog: NetworkProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = ""
        setContentView(R.layout.activity_common_lab)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        sessionManager = SessionManager(this)

        var addTest = findViewById<Button>(R.id.addTest)
        addTest.isClickable = true

        networkProgressDialog = NetworkProgressDialog(this)
        networkProgressDialog.show();

        rv = findViewById<RecyclerView>(R.id.testOrder)
        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        testTypeList = ArrayList<LabTestOrder>()
        encountersList = ArrayList<Encounter>()

        val testOrderSearch = RequestManager(
            applicationContext, sessionManager.getUsername(),
            sessionManager.getPassword()
        ).getPatientRetrofit().create(CommonLabApiService::class.java)

        testOrderSearch.getLabTestOrderByPatientUUID(
            "dbac89bb-508b-4693-aad1-3b5a5310252e",
            Constant.REPRESENTATION
        ).enqueue(object : Callback<CommonLabApiResponse> {
            override fun onResponse(
                call: Call<CommonLabApiResponse>,
                response: Response<CommonLabApiResponse>
            ) {
                Timber.e(response.message())
                if (response.isSuccessful) {
                    testTypeList = response.body()!!.labTests

                } else {
                    testTypeList = ArrayList<LabTestOrder>()
                }
                setTestOrderList()
            }

            override fun onFailure(call: Call<CommonLabApiResponse>, t: Throwable) {
                Timber.e(t.localizedMessage)


                setTestOrderList()
            }

        })




        addTest.setOnClickListener {
            addTest.isClickable = false
            networkProgressDialog.show();
            //Fetching Encounters
            testOrderSearch.getEncountersByPatientUUID(
                "dbac89bb-508b-4693-aad1-3b5a5310252e",
                Constant.REPRESENTATION
            ).enqueue(object : Callback<EncountersApiResponse> {
                override fun onResponse(
                    call: Call<EncountersApiResponse>,
                    response: Response<EncountersApiResponse>
                ) {
                    Timber.e(response.message())
                    if (response.isSuccessful) {
                        encountersList.clear()
                        encountersList = response.body()!!.encounters

                        if (encountersList.size != 0) {
                            var intent = Intent(this@CommonLabActivity, TestAdder::class.java)
                            val gson = Gson()
                            val encountersListJson: String = gson.toJson(encountersList)
                            intent.putExtra("encountersList", encountersListJson)
                            networkProgressDialog.dismiss()
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this@CommonLabActivity,
                                "No encounters for patient",
                                Toast.LENGTH_SHORT
                            ).show()
                            addTest.isClickable = true
                            networkProgressDialog.dismiss()
                        }

                    }
                }

                override fun onFailure(call: Call<EncountersApiResponse>, t: Throwable) {
                    Timber.e(t.localizedMessage)

                    addTest.isClickable = true
                    networkProgressDialog.dismiss()

                    Toast.makeText(
                        this@CommonLabActivity,
                        "Error fetching Encounters",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        }

    }

    fun setTestOrderList() {


        adapter =
            CommonLabAdapter(
                testTypeList, this, applicationContext
            )
        rv.adapter = adapter
        networkProgressDialog.dismiss()
    }


    override fun onBackPressed() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
        super.onBackPressed()

    }
}