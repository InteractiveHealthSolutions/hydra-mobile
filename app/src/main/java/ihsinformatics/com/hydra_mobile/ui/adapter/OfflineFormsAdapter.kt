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
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ihsinformatics.dynamicformsgenerator.Utils
import com.ihsinformatics.dynamicformsgenerator.common.Constants
import com.ihsinformatics.dynamicformsgenerator.data.database.OfflinePatient
import com.ihsinformatics.dynamicformsgenerator.data.database.SaveableForm
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames
import com.ihsinformatics.dynamicformsgenerator.network.pojos.PatientData
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.Patient
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.PatientApiResponse
import ihsinformatics.com.hydra_mobile.ui.activity.HomeActivity
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.PeriodType
import org.json.JSONArray
import org.json.JSONObject

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

        @RequiresApi(Build.VERSION_CODES.M) fun bindItems(form: SaveableForm) {

            if(form!=null && null != form.identifier) {
                tvPatientIdentifier.text = form.identifier

                formName.text = form.encounterType

            }
        }

    }

}