package ihsinformatics.com.hydra_mobile.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.Patient

class SearchPatientAdapter() : RecyclerView.Adapter<SearchPatientAdapter.ViewHolder>() {


    private var searchPatientList: List<Patient> = ArrayList()
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.context = parent.context
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
        val tvPatientAge = itemView.findViewById<TextView>(R.id.tv_patient_age)
        val tvPatientGender = itemView.findViewById<TextView>(R.id.tv_patient_gender)
        val tvPatientLocation = itemView.findViewById<TextView>(R.id.tv_patient_location)

        fun bindItems(patient: Patient) {

            tvPatientName.text = patient.displayName
            tvPatientAge.text = "23"
            tvPatientGender.text = "male"

        }

    }

}