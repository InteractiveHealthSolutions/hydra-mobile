package ihsinformatics.com.hydra_mobile.ui.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ihsinformatics.dynamicformsgenerator.data.pojos.SaveableForm
import ihsinformatics.com.hydra_mobile.R

class UploadFormsAdapter(saveableFormsList: ArrayList<SaveableForm>, c: Context) :
    RecyclerView.Adapter<UploadFormsAdapter.ViewHolder>() {


    private var formsList = saveableFormsList
    val context = c

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //this.context = parent.context
        val v = LayoutInflater.from(context)
            .inflate(R.layout.uploadable_forms_item_view, parent, false)
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
        val tvPatientName = itemView.findViewById<TextView>(R.id.tv_patient_name)
        val uploadErrorLayout = itemView.findViewById<LinearLayout>(R.id.uploadErrorLayout)
        val lastUploadError = itemView.findViewById<TextView>(R.id.lastUploadError)


        @RequiresApi(Build.VERSION_CODES.M)
        fun bindItems(form: SaveableForm) {

            if (form != null && null != form.identifier) {
                tvPatientIdentifier.text = form.identifier

                formName.text = form.encounterType

                tvPatientName.text=form.patient_name

                if(null!=form.lastUploadError && !form.lastUploadError.trim().equals(""))
                {
                    lastUploadError.setText(form.lastUploadError)
                    uploadErrorLayout.visibility=View.VISIBLE
                }
                else
                {
                    uploadErrorLayout.visibility=View.GONE
                }


            }
        }

    }

}