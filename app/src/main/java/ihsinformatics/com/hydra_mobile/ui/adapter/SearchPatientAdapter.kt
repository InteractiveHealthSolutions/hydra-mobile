package ihsinformatics.com.hydra_mobile.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ihsinformatics.dynamicformsgenerator.Utils
import com.ihsinformatics.dynamicformsgenerator.data.database.OfflinePatient
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.Patient
import ihsinformatics.com.hydra_mobile.ui.activity.HomeActivity
import org.json.JSONObject

class SearchPatientAdapter(patientSearched:List<Patient>, c: Context) : RecyclerView.Adapter<SearchPatientAdapter.ViewHolder>() {


    private var searchPatientList=patientSearched
    val context=c

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //this.context = parent.context
        val v = LayoutInflater.from(context)
            .inflate(R.layout.search_patient_item_view, parent, false)
        return ViewHolder(v)

    }

    override fun getItemCount(): Int {
        return searchPatientList.size
    }

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

        fun bindItems(patient: Patient) {

            tvPatientName.text = patient.display
            tvPatientAge.text = patient.person.getAge().toString()
            tvPatientIdentifier.text = patient.identifiers.get(0).display

            if(patient.person.getGender().toLowerCase().contains("f"))
            {
                genderImage.setImageDrawable(context.getDrawable(R.drawable.ic_female))
            }
            else
            {
                genderImage.setImageDrawable(context.getDrawable(R.drawable.ic_male))
            }

            searchLayout.setOnClickListener {

                var serverResponse: JSONObject? = null


                var offlinePatient=OfflinePatient(patient.identifiers.get(0).identifier,"","","","",0,patient.person.getDisplay(),patient.person.getGender(),1,null,null)

                val encounterCount = JSONObject()
                for (i in ParamNames.ENCOUNTER_TYPES.indices) {
                    encounterCount.put(ParamNames.ENCOUNTER_TYPES[i], 0)
                }
                offlinePatient.encounterJson = encounterCount.toString()

                serverResponse = Utils.converToServerResponse(offlinePatient)
                var requestType = ParamNames.GET_PATIENT_INFO

                Utils.convertPatientToPatientData(context,serverResponse, 0,requestType)
                context.startActivity(Intent(context,HomeActivity::class.java))
            }

        }

    }

}