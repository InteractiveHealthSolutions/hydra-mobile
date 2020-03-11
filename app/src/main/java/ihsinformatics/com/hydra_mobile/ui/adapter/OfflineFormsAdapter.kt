package ihsinformatics.com.hydra_mobile.ui.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ihsinformatics.dynamicformsgenerator.data.database.SaveableForm
import com.ihsinformatics.dynamicformsgenerator.data.utils.GlobalConstants
import com.ihsinformatics.dynamicformsgenerator.screens.Form
import ihsinformatics.com.hydra_mobile.R

class OfflineFormsAdapter(saveableFormsList: ArrayList<SaveableForm>, c: Context) : RecyclerView.Adapter<OfflineFormsAdapter.ViewHolder>() {


    private var formsList=saveableFormsList
    val context=c

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //this.context = parent.context
        val v = LayoutInflater.from(context)
            .inflate(R.layout.saveable_forms_item_view, parent, false)
        return ViewHolder(v)

    }

    override fun getItemCount(): Int {
        return formsList.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(formsList[position])
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val formName = itemView.findViewById<TextView>(R.id.formName)
        val tvPatientIdentifier = itemView.findViewById<TextView>(R.id.tv_patient_identifier)
        val singleLayout = itemView.findViewById<LinearLayout>(R.id.singleLayout)

        @RequiresApi(Build.VERSION_CODES.M) fun bindItems(form: SaveableForm) {

            if(form!=null && null != form.identifier) {
                tvPatientIdentifier.text = form.identifier

                formName.text = form.encounterType

                singleLayout.setOnClickListener{

                    var dataToLoad = "[\"Form Date: 11\\/03\\/2020\",\"Location: Beringharjo Market\",\"Geo Location: 24.8637077,67.0758411\",\"Disease site: PULMONARY TUBERCULOSIS\",\"Confirmation of MTB?: BACTERIOLOGICALLY CONFIRMED\",\"Method of Confirmation for Bacteriologically Positive Diagnosis: LPA TEST\",\"Type of TB According to Drug Sensitivity: CONFIRMED DRUG RESISTANT TB\",\"DR TB Registration Number: -\",\"Sub-classification for Confirmed Drug Resistant Cases: RIFAMPICIN RESISTANT TUBERCULOSIS INFECTION\",\"Patient Eligible to Start Treatment?: YES\",\"Is this an Index Patient?: YES\"]";

                    var intent = Intent(context, Form::class.java)
                    intent.putExtra(GlobalConstants.KEY_LOAD_DATA,true)
                    intent.putExtra(GlobalConstants.KEY_JSON_DATA,dataToLoad)
                    Form.setENCOUNTER_NAME(form.encounterType)
                    context.startActivity(intent)

                }
            }
        }

    }

}