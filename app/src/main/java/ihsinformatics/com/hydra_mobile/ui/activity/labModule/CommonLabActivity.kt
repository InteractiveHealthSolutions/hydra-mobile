package ihsinformatics.com.hydra_mobile.ui.activity.labModule

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.TestOrderModel
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.CommonLabApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.PatientApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.service.CommonLabApiService
import ihsinformatics.com.hydra_mobile.data.remote.service.PatientApiService
import ihsinformatics.com.hydra_mobile.ui.activity.HomeActivity
import ihsinformatics.com.hydra_mobile.ui.adapter.CommonLabAdapter
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import ihsinformatics.com.hydra_mobile.ui.viewmodel.PatientViewModel
import kotlinx.android.synthetic.main.activity_common_lab.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class CommonLabActivity : BaseActivity() {

    val TAG = "This is description"
    lateinit var testTypeList: ArrayList<TestOrderModel>
    lateinit var adapter: CommonLabAdapter
    lateinit var rv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = ""
        setContentView(R.layout.activity_common_lab)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        rv = findViewById<RecyclerView>(R.id.testOrder)
        rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


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
                        testTypeList = ArrayList<TestOrderModel>()
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