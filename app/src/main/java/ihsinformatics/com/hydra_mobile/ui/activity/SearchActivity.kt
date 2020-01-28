package ihsinformatics.com.hydra_mobile.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.Patient
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.PatientApiResponse
import ihsinformatics.com.hydra_mobile.ui.adapter.CommonLabAdapter
import ihsinformatics.com.hydra_mobile.ui.adapter.SearchPatientAdapter
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import ihsinformatics.com.hydra_mobile.ui.dialogs.NetworkProgressDialog
import ihsinformatics.com.hydra_mobile.ui.viewmodel.PatientViewModel

import kotlinx.android.synthetic.main.activity_search.*
import timber.log.Timber


class SearchActivity : BaseActivity(), View.OnClickListener {




    private lateinit var edtName: EditText
    private lateinit var edtContactNumber: EditText
    private lateinit var edtIdentifier: EditText
    private lateinit var patientViewModel: PatientViewModel
    private lateinit var searchPatientResultRecyclerView: RecyclerView
    private lateinit var patientSearchAdapter: SearchPatientAdapter
    private lateinit var btnSearch: Button

    private lateinit var patientSearchedList:List<Patient>

    private lateinit var networkProgressDialog: NetworkProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        initView()
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
        super.onBackPressed()
        startActivity(Intent(this, HomeActivity::class.java))
        finish()

    }

    private fun initView() {

        networkProgressDialog = NetworkProgressDialog(this)
        edtIdentifier = findViewById<EditText>(R.id.edt_search_by_identifier)
        btnSearch = findViewById<Button>(R.id.btn_patient_search)
        searchPatientResultRecyclerView = findViewById<RecyclerView>(R.id.rv_search_patient)

        searchPatientResultRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        patientViewModel = ViewModelProviders.of(this).get(PatientViewModel::class.java)

        btnSearch.setOnClickListener(this)
      //  var patientList=patientViewModel.getAllPatient()

           // for (i in patientList.indices) {
//                patientSearchAdapter.updatePatientList(patientList)
           // }



    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.btn_patient_search -> {
//                patientViewModel.search(edtIdentifier.text.toString())
//                var patientList=patientViewModel.getAllPatient()
//
//                patientSearchAdapter.updatePatientList(patientList)
                networkProgressDialog.show()
                searchPatientOnline(edtIdentifier.text.toString())
            }
        }

    }

    fun searchPatientOnline(queryString:String)
    {
        RequestManager(
            this, sessionManager.getUsername(),
            sessionManager.getPassword()
                      ).searchPatient(Constant.REPRESENTATION, queryString,
            object : RESTCallback {
                override fun <T> onSuccess(o: T) {
                    try {
                        val response = (o as PatientApiResponse)
                        patientSearchedList=response.results
                        setPatientSearchedList()
                    } catch (e: Exception) {
                        Timber.e(e.localizedMessage)

                        networkProgressDialog.dismiss()
                    }
                }

                override fun onFailure(t: Throwable) {
                    Timber.e(t.localizedMessage)
                    networkProgressDialog.dismiss()
                }
            })
    }

    fun setPatientSearchedList() {

        patientSearchAdapter = SearchPatientAdapter(patientSearchedList, this)
        searchPatientResultRecyclerView.adapter = patientSearchAdapter
        networkProgressDialog.dismiss()
    }

}
