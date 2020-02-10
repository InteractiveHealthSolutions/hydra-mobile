package ihsinformatics.com.hydra_mobile.ui.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.ihsinformatics.dynamicformsgenerator.PatientInfoFetcher
import com.ihsinformatics.dynamicformsgenerator.Utils
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.PatientApiResponse
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


    private lateinit var qrReader: ImageView
    private lateinit var qrTextView: TextView
    private lateinit var dialog: Dialog

    private lateinit var patientSearchedList: PatientApiResponse

    private lateinit var networkProgressDialog: NetworkProgressDialog

    private val MY_CAMERA_REQUEST_CODE = 100

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


        qrReader = findViewById<View>(R.id.qrReader) as ImageView
        qrTextView = findViewById<View>(R.id.qrtextview) as TextView

        qrReader.setOnClickListener(this)
        qrTextView.setOnClickListener(this)

        qrReader.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 23) {
                checkCameraPermission()
            } else {
                showQRCodeReaderDialog()
            }
        }

        qrTextView.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 23) {
                checkCameraPermission()
            } else {
                showQRCodeReaderDialog()
            }
        }


    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.btn_patient_search -> {

                if (isInternetConnected()) {
                    networkProgressDialog.show()

                    searchPatientOnline(edtIdentifier.text.toString())
                }else
                {
                    ToastyWidget.getInstance().displayError(this,getString(R.string.internet_issue),Toast.LENGTH_LONG)
                }
            }
        }

    }

    fun searchPatientOnline(queryString: String) {
        RequestManager(this, sessionManager.getUsername(), sessionManager.getPassword()).searchPatient(Constant.REPRESENTATION, queryString, object : RESTCallback {
            override fun <T> onSuccess(o: T) {
                try {
                    val response = (o as PatientApiResponse)
                    patientSearchedList = response
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


    protected fun checkCameraPermission() {
        val hasWriteContactsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale((this as Activity?)!!, Manifest.permission.CAMERA)) {
                Utils.showMessageOKCancel(this, resources.getString(com.ihsinformatics.dynamicformsgenerator.R.string.qr_code_permission_request_message)) { dialog, which -> ActivityCompat.requestPermissions((this as Activity?)!!, arrayOf(Manifest.permission.CAMERA), this.MY_CAMERA_REQUEST_CODE) }
                return
            }
            ActivityCompat.requestPermissions((this as Activity?)!!, arrayOf(Manifest.permission.CAMERA), this.MY_CAMERA_REQUEST_CODE)
            return
        }
        showQRCodeReaderDialog()
    }

    fun showQRCodeReaderDialog() {
        dialog = Dialog(this)
        dialog.setContentView(com.ihsinformatics.dynamicformsgenerator.R.layout.dialog_qrcode)
        dialog.show()
        val qrCodeReaderView: QRCodeReaderView
        qrCodeReaderView = dialog.findViewById(com.ihsinformatics.dynamicformsgenerator.R.id.qrdecoderview)
        qrCodeReaderView.startCamera()
        qrCodeReaderView.setQRDecodingEnabled(true)
        qrCodeReaderView.setAutofocusInterval(2000L)
        qrCodeReaderView.setTorchEnabled(true)
        qrCodeReaderView.setFrontCamera()
        qrCodeReaderView.setBackCamera()
        qrCodeReaderView.setOnQRCodeReadListener { text, points ->
            edtIdentifier.setText(text)
            edtIdentifier.setEnabled(true)
            qrCodeReaderView.stopCamera()
            dialog.dismiss()
        }
    }

}
