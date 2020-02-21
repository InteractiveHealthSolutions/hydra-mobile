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
import com.ihsinformatics.dynamicformsgenerator.common.Constants
import com.ihsinformatics.dynamicformsgenerator.data.database.OfflinePatient
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames
import com.ihsinformatics.dynamicformsgenerator.network.pojos.PatientData
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.Patient
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.PatientApiResponse
import ihsinformatics.com.hydra_mobile.ui.activity.HomeActivity
import org.json.JSONArray
import org.json.JSONObject

class OfflinePatientAdapter(offlinePatientList: ArrayList<PatientData>, c: Context) : RecyclerView.Adapter<OfflinePatientAdapter.ViewHolder>() {


    private var searchPatientList=offlinePatientList
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


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPatientName = itemView.findViewById<TextView>(R.id.tv_patient_name)
        val tvPatientIdentifier = itemView.findViewById<TextView>(R.id.tv_patient_identifier)
        val tvPatientAge = itemView.findViewById<TextView>(R.id.tv_patient_age)
        val genderImage = itemView.findViewById<ImageView>(R.id.genderImage)
        val searchLayout = itemView.findViewById<LinearLayout>(R.id.searchLayout)

        fun bindItems(patient: PatientData) {

            if(patient!=null && null != patient.identifiers && patient.identifiers.size>0) {
                tvPatientName.text = patient.patient.givenName +" "+patient.patient.familyName
                tvPatientAge.text = patient.patient.age.toString()
                if (null != patient.patient.identifier) tvPatientIdentifier.text = patient.patient.identifier

                if (patient.patient.getGender().toLowerCase().contains("f")) {
                    genderImage.setImageDrawable(context.getDrawable(R.drawable.ic_female))
                } else {
                    genderImage.setImageDrawable(context.getDrawable(R.drawable.ic_male))
                }

                searchLayout.setOnClickListener {
                    Global.patientData=patient
                    context.startActivity(Intent(context, HomeActivity::class.java))
                }
            }
        }

    }

}