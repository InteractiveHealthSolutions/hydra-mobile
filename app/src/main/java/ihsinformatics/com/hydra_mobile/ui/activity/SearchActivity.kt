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
import androidx.recyclerview.widget.RecyclerView
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.ui.adapter.SearchPatientAdapter
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import ihsinformatics.com.hydra_mobile.ui.viewmodel.PatientViewModel

import kotlinx.android.synthetic.main.activity_search.*


class SearchActivity : BaseActivity(), View.OnClickListener {




    private lateinit var edtName: EditText
    private lateinit var edtContactNumber: EditText
    private lateinit var edtIdentifier: EditText
    private lateinit var patientViewModel: PatientViewModel
    private lateinit var searchPatientResultRecyclerView: RecyclerView
    private lateinit var patientSearchAdapter: SearchPatientAdapter
    private lateinit var btnSearch: Button

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
        edtName = findViewById<EditText>(R.id.edt_search_by_name)
        edtContactNumber = findViewById<EditText>(R.id.edt_search_by_number)
        edtIdentifier = findViewById<EditText>(R.id.edt_search_by_identifier)
        btnSearch = findViewById<Button>(R.id.btn_patient_search)
        searchPatientResultRecyclerView = findViewById<RecyclerView>(R.id.rv_search_patient)
        patientSearchAdapter = SearchPatientAdapter()
        patientViewModel = ViewModelProviders.of(this).get(PatientViewModel::class.java)

        btnSearch.setOnClickListener(this)
        var patientList=patientViewModel.getAllPatient()

           // for (i in patientList.indices) {
                patientSearchAdapter.updatePatientList(patientList)
           // }



    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.btn_patient_search -> {
                patientViewModel.search(edtIdentifier.text.toString())
                var patientList=patientViewModel.getAllPatient()

                patientSearchAdapter.updatePatientList(patientList)
            }
        }

    }
}
