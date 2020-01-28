package ihsinformatics.com.hydra_mobile.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.Encounter
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestOrder
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.TestSample
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.ManageTestSample
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.TestSampleAdder
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.TestSampleResult
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.TestSummary
import ihsinformatics.com.hydra_mobile.utils.SessionManager


class ReportAdapter(
    val encountersList: List<Encounter>, c: Context
                   ) : RecyclerView.Adapter<ReportAdapter.SingleItemTestHolder>() {


    override fun onBindViewHolder(holder: SingleItemTestHolder, position: Int) {
        holder.encounterName?.text = encountersList[position].display
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleItemTestHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.report_item_layout, parent, false)
        return SingleItemTestHolder(v)

    }

    override fun getItemCount(): Int {
        return encountersList.size
    }

    inner class SingleItemTestHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val encounterName = itemView.findViewById<TextView>(R.id.encounterName)

    }

}


