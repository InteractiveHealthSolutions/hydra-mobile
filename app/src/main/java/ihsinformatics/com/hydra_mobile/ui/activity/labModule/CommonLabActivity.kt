package ihsinformatics.com.hydra_mobile.ui.activity.labModule

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestOrder
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.CommonLabApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.service.CommonLabApiService
import ihsinformatics.com.hydra_mobile.ui.activity.HomeActivity
import ihsinformatics.com.hydra_mobile.ui.adapter.CommonLabAdapter
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import kotlinx.android.synthetic.main.activity_common_lab.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class CommonLabActivity : AppCompatActivity() {

    val TAG = "This is description"
    lateinit var testTypeList: ArrayList<LabTestOrder>
    lateinit var adapter: CommonLabAdapter
    lateinit var rv: RecyclerView
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = ""
        setContentView(R.layout.activity_common_lab)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        sessionManager = SessionManager(this)

        rv = findViewById<RecyclerView>(R.id.testOrder)
        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        testTypeList = ArrayList<LabTestOrder>()

        val testOrderSearch = RequestManager(
            applicationContext, sessionManager.getUsername(),
            sessionManager.getPassword()
        ).getPatientRetrofit().create(CommonLabApiService::class.java)

        testOrderSearch.getLabTestOrderByPatientUUID(
            "dbac89bb-508b-4693-aad1-3b5a5310252e",
            Constant.REPRESENTATION
        )
            .enqueue(object : Callback<CommonLabApiResponse> {
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


        var addTest = findViewById<Button>(R.id.addTest)

        addTest.setOnClickListener {

            startActivity(Intent(this@CommonLabActivity, TestAdder::class.java))

        }

    }

    fun setTestOrderList() {


        adapter =
            CommonLabAdapter(
                testTypeList, this
            )
        rv.adapter = adapter

    }


    override fun onBackPressed() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
        super.onBackPressed()

    }
}