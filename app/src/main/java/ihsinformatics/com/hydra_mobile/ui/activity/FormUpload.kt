package ihsinformatics.com.hydra_mobile.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ihsinformatics.dynamicformsgenerator.Utils.isInternetConnected
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess
import com.ihsinformatics.dynamicformsgenerator.data.database.SaveableForm
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import com.ihsinformatics.dynamicformsgenerator.utils.Logger
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.FormSubmissionReqBody
import ihsinformatics.com.hydra_mobile.data.remote.model.formSubmission
import ihsinformatics.com.hydra_mobile.data.remote.service.FormSubmissionApiService
import ihsinformatics.com.hydra_mobile.ui.adapter.EditFormsAdapter
import ihsinformatics.com.hydra_mobile.ui.adapter.UploadFormsAdapter
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormUpload : AppCompatActivity() {

    private lateinit var uploadFormsRecyclerView: RecyclerView
    private lateinit var uploadFormsAdapter: UploadFormsAdapter

    private lateinit var sessionManager: SessionManager

    private var saveableOfflineForms = ArrayList<SaveableForm>()

    private lateinit var uploadForms: LinearLayout

    private var sentCreatePatientsCount: Int = 0
    private var sentEncountersCount: Int = 0

    private var createPatientForm: List<SaveableForm> = arrayListOf<SaveableForm>();
    private var encounterForms: List<SaveableForm> = arrayListOf<SaveableForm>();


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_upload)

        sessionManager = SessionManager(this)

        uploadFormsRecyclerView = findViewById<RecyclerView>(R.id.offline_forms)
        uploadForms = findViewById<LinearLayout>(R.id.upload)

        uploadFormsRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        uploadForms.setOnClickListener {

            sentCreatePatientsCount = 0

            // get all non uploaded form from database
            val saveableForms = DataAccess.getInstance()
                .getAllFormsByHydraUrl(this, Global.HYRDA_CURRENT_URL)

            // separate createPatientForms and encounterForms
            createPatientForm = arrayListOf<SaveableForm>()
            encounterForms = arrayListOf<SaveableForm>()
            for (i in saveableForms) {
                if (isInternetConnected(this)) {
                    if (i.encounterType.equals(ParamNames.ENCOUNTER_TYPE_CREATE_PATIENT)) {
                        (createPatientForm as ArrayList<SaveableForm>).add(i)
                    } else {
                        (encounterForms as ArrayList<SaveableForm>).add(i)
                    }
                }
            }

            // start uploading forms
            initDataUpload()


        }

    }

    private fun initDataUpload() {
        if (isInternetConnected(this)) {
            if (createPatientForm.size > 0) sendData(createPatientForm.get(0))
            else if (encounterForms.size > 0) sendData(encounterForms.get(0))
        } else {
            ToastyWidget.getInstance()
                .displayError(this, getString(R.string.internet_issue), Toast.LENGTH_SHORT)
        }
    }


    val failedIdsList = arrayListOf<String>()
    private fun sendData(saveableForm: SaveableForm) {

        Logger.logEvent("FORM_UPLOAD_ATTEMPTED", saveableForm.getFormData().toString())
        val dataSender = RequestManager(applicationContext, sessionManager.getUsername(), sessionManager.getPassword()).getFormRetrofit()
            .create(FormSubmissionApiService::class.java)
        val dataArray = saveableForm.formData!!.getJSONArray(ParamNames.DATA).toString()
        val metadata = saveableForm.formData!!.getJSONObject(ParamNames.METADATA).toString()

        val requestBody = FormSubmissionReqBody(dataArray, metadata)
        //val body: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBody)

        dataSender.submitForm(requestBody).enqueue(object : Callback<formSubmission> {
            override fun onFailure(call: Call<formSubmission>, t: Throwable) {
                val metaData: JSONObject = saveableForm.formData!!.getJSONObject(ParamNames.METADATA)
                if (metaData.has("mrNumber")) {
                    val failedIdentifier: String = metaData.getString("mrNumber")
                    failedIdsList.add(failedIdentifier)
                }
                saveableForm.lastUploadError= "Some Error occured"
                DataAccess.getInstance().insertForm(this@FormUpload,saveableForm)
                ToastyWidget.getInstance()
                    .displayError(this@FormUpload, "Error", Toast.LENGTH_SHORT)


                doPostResponse(metaData)
            }

            override fun onResponse(call: Call<formSubmission>, response: Response<formSubmission>) {
                val metaData: JSONObject = saveableForm.formData!!.getJSONObject(ParamNames.METADATA)
                if (response.isSuccessful) {
                    ToastyWidget.getInstance()
                        .displaySuccess(this@FormUpload, "Success", Toast.LENGTH_SHORT)
                    DataAccess.getInstance()
                        .deleteFormByFormID(this@FormUpload, saveableForm.formId)
                    Logger.logEvent("FORM_UPLOAD_SUCCESS", saveableForm.getFormData().toString())

                } else {
                    if (metaData.has("mrNumber")) {
                        val failedIdentifier: String = metaData.getString("mrNumber")
                        failedIdsList.add(failedIdentifier)
                    }

                    ToastyWidget.getInstance()
                        .displayError(this@FormUpload, "Server error", Toast.LENGTH_SHORT)
                    saveableForm.lastUploadError= filterErrorMessage(response.errorBody()!!.string())
                    DataAccess.getInstance().insertForm(this@FormUpload,saveableForm)
                    Logger.logEvent("FORM_UPLOAD_FAILED", saveableForm.getFormData().toString())
                }

                doPostResponse(metaData)
                setFormsList()
            }
        })
    }


    private fun filterErrorMessage(errorMsg:String):String
    {
        if(errorMsg.contains("failed to validate with reason: Identifier")){
            return "Duplicate Identifer"
        }
        else if(errorMsg.contains("failed to validate with reason: birthdate"))
        {
            return "Future Date Selected"
        }
        else if(errorMsg.contains("failed to validate with reason: encounterDatetime"))
        {
            return "OpenMRS Date and Time Issue"
        }
        else{
            return "Some Error Occured"
        }
    }


    private fun doPostResponse(metadata: JSONObject) {
        if (metadata.has("mrNumber")) { // means its a createPatientForm
            sentCreatePatientsCount++
        } else { // means its an encounterForm
            sentEncountersCount++
        }
        if (isInternetConnected(this)) {
            if (createPatientForm.size == sentCreatePatientsCount) { // All create patient forms are uploaded, send encounter form
                if (sentEncountersCount < encounterForms.size)
                    sendData(encounterForms.get(sentEncountersCount))
            } else { // All create patient forms are not uploaded, upload the remaining ones
                sendData(createPatientForm.get(sentCreatePatientsCount))
            }
        } else {
            ToastyWidget.getInstance()
                .displayError(this@FormUpload, getString(R.string.internet_issue), Toast.LENGTH_SHORT)
        }
    }


    @Deprecated("Changed the flow for the form uploading, this functions is useless now")
    private fun initSendEncounters() {

        val saveableForms = DataAccess.getInstance()
            .getAllFormsByHydraUrl(this, Global.HYRDA_CURRENT_URL)

        for (i in saveableForms) {
            if (isInternetConnected(this)) {
                val metaData: JSONObject = i.formData.getJSONObject("metadata")
                if (metaData.has("mrNumber")) // this means it is a patient creation form
                    continue
                val identifier: String = metaData.getJSONObject("patient")
                    .getJSONArray("identifiers").getJSONObject(0).getString("value")
                if (!failedIdsList.contains(identifier)) sendData(i)
            } else {
                ToastyWidget.getInstance()
                    .displayError(this, getString(R.string.internet_issue), Toast.LENGTH_SHORT)
            }
        }

    }


    fun setFormsList() {

        saveableOfflineForms = DataAccess.getInstance()
            .getAllFormsByHydraUrl(this, Global.HYRDA_CURRENT_URL) as ArrayList<SaveableForm>
        uploadFormsAdapter = UploadFormsAdapter(saveableOfflineForms, this)
        uploadFormsRecyclerView.adapter = uploadFormsAdapter

    }

    override fun onResume() {
        super.onResume()
        setFormsList()
    }
}