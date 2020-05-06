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
import com.google.zxing.Result
import com.ihsinformatics.dynamicformsgenerator.Utils
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess
import com.ihsinformatics.dynamicformsgenerator.data.database.OfflinePatient
import com.ihsinformatics.dynamicformsgenerator.data.utils.JsonHelper
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames
import com.ihsinformatics.dynamicformsgenerator.network.pojos.PatientData
import com.ihsinformatics.dynamicformsgenerator.utils.Logger
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.PatientApiResponse
import ihsinformatics.com.hydra_mobile.ui.adapter.OfflinePatientAdapter
import ihsinformatics.com.hydra_mobile.ui.adapter.SearchPatientAdapter
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import ihsinformatics.com.hydra_mobile.ui.dialogs.NetworkProgressDialog
import ihsinformatics.com.hydra_mobile.ui.viewmodel.PatientViewModel
import kotlinx.android.synthetic.main.activity_search.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.PeriodType
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber


class SearchActivity : BaseActivity(), View.OnClickListener, ZXingScannerView.ResultHandler {


    private var mScannerView: ZXingScannerView? = null
    protected var dialogBarCodeAndQRCode: Dialog? = null

    private lateinit var edtIdentifier: EditText
    private lateinit var patientViewModel: PatientViewModel
    private lateinit var searchPatientResultRecyclerView: RecyclerView
    private lateinit var offlinePatientResultRecyclerView: RecyclerView
    private lateinit var nothingToShow: TextView
    private lateinit var recyclerLayout: LinearLayout
    private lateinit var patientSearchAdapter: SearchPatientAdapter
    private lateinit var btnSearch: Button

    private var offlinePatientList = ArrayList<PatientData>()
    private lateinit var offlinePatientAdapter: OfflinePatientAdapter

    private lateinit var qrReader: ImageView
    private lateinit var qrTextView: TextView
    private lateinit var dialog: Dialog

    private var patientSearchedList: PatientApiResponse? = null

    private lateinit var networkProgressDialog: NetworkProgressDialog

    private val MY_CAMERA_REQUEST_CODE = 100

    var requestType: String? = null




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

        mScannerView = ZXingScannerView(this)
        dialogBarCodeAndQRCode = Dialog(this)
        dialogBarCodeAndQRCode!!.setContentView(mScannerView)

        networkProgressDialog = NetworkProgressDialog(this)
        edtIdentifier = findViewById<EditText>(R.id.edt_search_by_identifier)
        btnSearch = findViewById<Button>(R.id.btn_patient_search)

        nothingToShow = findViewById(R.id.nothingToShow)
        recyclerLayout = findViewById(R.id.recyclerLayout)

        searchPatientResultRecyclerView = findViewById<RecyclerView>(R.id.rv_search_patient)
        searchPatientResultRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        offlinePatientResultRecyclerView = findViewById<RecyclerView>(R.id.offline_search)
        offlinePatientResultRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


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
                // showQRCodeReaderDialog()
                showQRAndBarCodeReaderDialog()
            }
        }

        qrTextView.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 23) {
                checkCameraPermission()
            } else {
               // showQRCodeReaderDialog()
                showQRAndBarCodeReaderDialog()
            }
        }

    }

    private fun setVisibilities() {

        nothingToShow.visibility = View.GONE
        recyclerLayout.visibility=View.VISIBLE

        if (null != patientSearchedList && patientSearchedList!!.results != null && patientSearchedList!!.results.size > 0) {
            searchPatientResultRecyclerView.visibility = View.VISIBLE
        } else {
            searchPatientResultRecyclerView.visibility = View.GONE
        }

        if (null != offlinePatientList && offlinePatientList.size > 0) {
            offlinePatientResultRecyclerView.visibility = View.VISIBLE
        } else {
            offlinePatientResultRecyclerView.visibility = View.GONE

            if (searchPatientResultRecyclerView.visibility == View.GONE) {
                nothingToShow.visibility = View.VISIBLE

                recyclerLayout.visibility=View.GONE
            }
        }


    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.btn_patient_search -> {

                offlinePatientList.clear()

                if (null != edtIdentifier.text.toString() && !edtIdentifier.text.toString().equals("") && !edtIdentifier.text.toString().equals(" ")) {

                    if (isInternetConnected()) {
                        networkProgressDialog.show()
                        searchPatientOnline(edtIdentifier.text.toString())
                    }

                    var offlinePatient: OfflinePatient? = null
                    offlinePatient = DataAccess.getInstance().getPatientByMRNumber(this, edtIdentifier.getText().toString())
                    if (null != offlinePatient) initOfflinePatientList(offlinePatient)

                    var offlinePatientList: List<OfflinePatient>
                    offlinePatientList = DataAccess.getInstance().getPatientMatchesByName(this, edtIdentifier.getText().toString())
                    if (null != offlinePatientList && offlinePatientList.size != 0) {

                        for(offPat in offlinePatientList)
                        initOfflinePatientList(offPat)
                    }

                } else {
                    edtIdentifier.error = resources.getString(ihsinformatics.com.hydra_mobile.R.string.empty_field)
                    edtIdentifier.requestFocus()
                }

            }
        }

    }

    private fun initOfflinePatientList(offlinePatient: OfflinePatient) {

        networkProgressDialog.show()
        var serverResponse: JSONObject? = null
        serverResponse = Utils.converToServerResponse(offlinePatient)
        requestType = ParamNames.GET_PATIENT_INFO
        convertOfflinePatientToPatient(serverResponse, 0)
        setOfflinePatientSearchedList()


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
        patientSearchAdapter = patientSearchedList?.let { SearchPatientAdapter(it, this, sessionManager.getUsername(), sessionManager.getPassword()) }!!
        searchPatientResultRecyclerView.adapter = patientSearchAdapter
        setVisibilities()
        networkProgressDialog.dismiss()
    }

    fun setOfflinePatientSearchedList() {
        offlinePatientAdapter = OfflinePatientAdapter(offlinePatientList, this)
        offlinePatientResultRecyclerView.adapter = offlinePatientAdapter
        setVisibilities()
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
        //showQRCodeReaderDialog()
        showQRAndBarCodeReaderDialog()
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


    fun convertOfflinePatientToPatient(resp: JSONObject, respId: Int) {
        networkProgressDialog.dismiss()
        try {
            if (!resp.has(ParamNames.SERVER_ERROR)) {
                if (requestType == ParamNames.GET_PATIENT_INFO) {
                    var patientData: PatientData? = null
                    val patient = JsonHelper.getInstance(this).ParsePatientFromUser(resp)
                    val offlinePatient = OfflinePatient()
                    if (patient != null) {
                        patientData = PatientData(patient)
                        val identifiers = resp.optJSONArray(ParamNames.PATIENT).getJSONObject(0).optJSONArray(ParamNames.IDENTIFIERS)
                        if (identifiers != null) for (i in 0 until identifiers.length()) {
                            val id = identifiers.getJSONObject(i)
                            val identifier = id.optString(ParamNames.IDENTIFIER)
                            val idType = id.getJSONObject(ParamNames.IDENTIFIER_TYPE)
                            val identifierType = idType.getString(ParamNames.DISPLAY)
                            patientData.addIdentifier(identifierType, identifier)
                            if (identifierType == ParamNames.INDUS_PROJECT_IDENTIFIER) {
                                offlinePatient.mrNumber = identifier
                            }
                        }
                    }
                    val encounters = resp.getJSONObject(ParamNames.ENCOUNTERS_COUNT) as JSONObject

                    var years = 0

                    if (offlinePatient.mrNumber != null) {
                        offlinePatient.encounterJson = encounters.toString()
                        offlinePatient.fieldDataJson = generateFieldsJon(resp).toString()
                        offlinePatient.name = patient!!.givenName + " " + patient.familyName
                        offlinePatient.gender = patient.gender
                        offlinePatient.dob = patient.birthDate.time


                        val birthDate = patient.birthDate
                        val birthTime = DateTime(birthDate)
                        val nowTime = DateTime()
                        val interval = Interval(birthTime, nowTime)
                        val period = interval.toPeriod().normalizedStandard(PeriodType.yearMonthDay())
                        years = period.getYears()




                        DataAccess.getInstance().insertOfflinePatient(this, offlinePatient)
                    }
                    if (patientData != null) {
                        patientData.patient.age = years
                        offlinePatientList.add(patientData)
                    }

                }
            } else {
                val value: String
                value = resp.getString(ParamNames.SERVER_ERROR)
                ToastyWidget.getInstance().displayError(this, value, Toast.LENGTH_LONG)
            }
        } catch (e: JSONException) {
            ToastyWidget.getInstance().displayError(this, "Could not parse server response", Toast.LENGTH_LONG)
            Logger.log(e)
        }
    }


    private fun generateFieldsJon(resp: JSONObject): JSONObject? {
        resp.remove(ParamNames.ENCOUNTERS_COUNT)
        resp.remove(ParamNames.PATIENT)
        return resp
    }


    fun showQRAndBarCodeReaderDialog() {
        mScannerView!!.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView!!.stopCamera()
        mScannerView!!.startCamera() // Start camera on resume
        dialogBarCodeAndQRCode!!.show()
    }


    override fun handleResult(p0: Result?) {
        edtIdentifier.setText(p0!!.getText())
        edtIdentifier.setEnabled(true)
        mScannerView!!.stopCamera()
        dialogBarCodeAndQRCode!!.dismiss()

    }

}
