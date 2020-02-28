package ihsinformatics.com.hydra_mobile.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.model.history.Encounters


class ReportAdapter(
    val encountersList: List<Encounters>, c: Context
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


