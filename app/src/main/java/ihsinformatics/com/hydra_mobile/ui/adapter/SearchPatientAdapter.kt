package ihsinformatics.com.hydra_mobile.ui.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ihsinformatics.dynamicformsgenerator.Utils
import com.ihsinformatics.dynamicformsgenerator.common.Constants
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess
import com.ihsinformatics.dynamicformsgenerator.data.database.history.Encounters
import com.ihsinformatics.dynamicformsgenerator.data.database.OfflinePatient
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.ReportEncountersApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.Patient
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.PatientApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.service.CommonLabApiService
import ihsinformatics.com.hydra_mobile.ui.activity.HomeActivity
import ihsinformatics.com.hydra_mobile.ui.dialogs.NetworkProgressDialog
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.PeriodType
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class SearchPatientAdapter(patientSearched: PatientApiResponse, c: Context, username: String, password: String) : RecyclerView.Adapter<SearchPatientAdapter.ViewHolder>() {


    private var resultFromAPI = patientSearched
    private var searchPatientList = patientSearched.results
    private val username = username
    private val password = password
    var encountersList = ArrayList<Encounters>()


    private val networkProgressDialog = NetworkProgressDialog(c)

    val context = c

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //this.context = parent.context
        val v = LayoutInflater.from(context).inflate(R.layout.search_patient_item_view, parent, false)
        return ViewHolder(v)

    }

    override fun getItemCount(): Int {
        return searchPatientList.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(searchPatientList[position])
    }

    fun updatePatientList(patients: List<Patient>) {
        this.searchPatientList = patients
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPatientName = itemView.findViewById<TextView>(R.id.tv_patient_name)
        val tvPatientIdentifier = itemView.findViewById<TextView>(R.id.tv_patient_identifier)
        val tvPatientAge = itemView.findViewById<TextView>(R.id.tv_patient_age)
        val genderImage = itemView.findViewById<ImageView>(R.id.genderImage)
        val searchLayout = itemView.findViewById<LinearLayout>(R.id.searchLayout)
        val mode = itemView.findViewById<TextView>(R.id.mode)


        @RequiresApi(Build.VERSION_CODES.M) fun bindItems(patient: Patient) {

            if (patient != null && null != patient.identifiers && patient.identifiers.size > 0) {
                tvPatientName.text = patient.identifiers.get(0).identifier

                if (!patient.person.getBirthDate().isNullOrBlank())
                    {
                        val birthTime = DateTime(patient.person.getBirthDate())
                        val nowTime = DateTime()
                        val interval = Interval(birthTime, nowTime)
                        val period = interval.toPeriod().normalizedStandard(PeriodType.yearMonthDay())
                        val years = period.getYears()
                        val months = period.getMonths()
                        val days = period.getDays()
                        tvPatientAge.setText(years.toString() + " years, " + months.toString() + " months")
                    }

                    if (null != patient.identifiers.get(0)) tvPatientIdentifier.text = patient.person.getDisplay()

                    if (patient.person.getGender().toLowerCase().contains("f")) {
                        genderImage.setImageDrawable(context.getDrawable(R.drawable.ic_female))
                    } else {
                        genderImage.setImageDrawable(context.getDrawable(R.drawable.ic_male))
                    }

                    mode.setText(context.getString(R.string.online))
                    mode.setTextColor(ContextCompat.getColor(context, R.color.Lime))

                    searchLayout.setOnClickListener {

                        if (null != patient.identifiers.get(0)) {
                            val testOrderSearch = RequestManager(context, username, password).getPatientRetrofit().create(CommonLabApiService::class.java)

                            testOrderSearch.getEncountersOfPatient(patient.identifiers.get(0).identifier, Constant.REPRESENTATION).enqueue(object : Callback<ReportEncountersApiResponse> {
                                override fun onResponse(
                                    call: Call<ReportEncountersApiResponse>, response: Response<ReportEncountersApiResponse>
                                                       ) {
                                    Timber.e(response.message())
                                    if (response.isSuccessful) {

                                        encountersList = response.body()!!.encounters
                                        DataAccess.getInstance().insertServiceHistory(context, patient.identifiers.get(0).identifier, encountersList);
                                        initializePatient(patient)

                                    }
                                }

                                override fun onFailure(call: Call<ReportEncountersApiResponse>, t: Throwable) {
                                    Timber.e(t.localizedMessage)

                                    networkProgressDialog.dismiss()

                                    Toast.makeText(context, "Error fetching Encounters", Toast.LENGTH_SHORT).show()
                                }
                            })

                        }
                    }
                }
            }

        }


        fun initializePatient(patient: Patient) {
            var serverResponse: JSONObject? = null
            var dob = Global.OPENMRS_TIMESTAMP_FORMAT.parse(patient.person.getBirthDate()).time

            var offlinePatient = OfflinePatient(patient.identifiers.get(0).identifier, "", "", "", "", 0, patient.person.getDisplay(), patient.person.getGender(), dob, null, null)


            //Initialization of summary fields
            var fieldJsonString = offlinePatient.fieldDataJson
            if (fieldJsonString == null) fieldJsonString = JSONObject().toString()
            val existingFieldsJson = JSONObject(fieldJsonString)
            for (i in ParamNames.SUMMARY_VARIBALES) {
                existingFieldsJson.put(i, "")
            }
            for (i in ParamNames.SUMMARY_VARIABLES_OBJECTS) {
                existingFieldsJson.put(i, JSONObject())
            }
            for (i in ParamNames.SUMMARY_VARIABLES_ARRAYS) {
                existingFieldsJson.put(i, JSONArray())
            }
            offlinePatient.fieldDataJson = existingFieldsJson.toString()


            //Initialization of all filled encounters count via this device as 0
            val encounterCount = JSONObject()
            val encounters = Constants.getEncounterTypes().values
            for (i in encounters) {
                encounterCount.put(i, 0)
            }
            offlinePatient.encounterJson = encounterCount.toString()

            serverResponse = Utils.converToServerResponse(offlinePatient)
            var requestType = ParamNames.GET_PATIENT_INFO

            Utils.convertPatientToPatientData(context, serverResponse, 0, requestType)

            networkProgressDialog.dismiss()
            context.startActivity(Intent(context, HomeActivity::class.java))
        }


    }