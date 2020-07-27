package ihsinformatics.com.hydra_mobile.ui.adapter

import android.app.AlertDialog
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
import androidx.recyclerview.widget.RecyclerView
import com.ihsinformatics.dynamicformsgenerator.Utils
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess
import com.ihsinformatics.dynamicformsgenerator.data.database.SaveableForm
import com.ihsinformatics.dynamicformsgenerator.data.utils.GlobalConstants
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames
import com.ihsinformatics.dynamicformsgenerator.screens.Form
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.R

class EditFormsAdapter(saveableFormsList: ArrayList<SaveableForm>, c: Context) :
    RecyclerView.Adapter<EditFormsAdapter.ViewHolder>() {


    private var formsList = saveableFormsList
    val context = c

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
        val tvPatientName = itemView.findViewById<TextView>(R.id.tv_patient_name)
        val singleLayout = itemView.findViewById<LinearLayout>(R.id.singleLayout)
        val deleteLayout = itemView.findViewById<ImageView>(R.id.deleteTrash)


        @RequiresApi(Build.VERSION_CODES.M)
        fun bindItems(form: SaveableForm) {

            if (form != null && null != form.identifier) {
                tvPatientIdentifier.text = form.identifier

                formName.text = form.encounterType

                tvPatientName.text = form.patient_name

                singleLayout.setOnClickListener {

                    val offlinePatient = DataAccess.getInstance()
                        .getPatientByMRNumber(context, form.identifier)

                    val serverResponse = Utils.converToServerResponse(offlinePatient)

                    val requestType = ParamNames.GET_PATIENT_INFO

                    Utils.convertPatientToPatientData(context, serverResponse, 0, requestType)

                    if (Global.patientData != null && Global.patientData.patient.identifier.equals(
                            form.identifier
                        )
                    ) {

//                        val dataInJson = JSONObject(form.formData)
//                        val dataToLoad = dataInJson.optJSONArray("values").toString()

                        var intent = Intent(context, Form::class.java)
                        intent.putExtra(GlobalConstants.KEY_LOAD_DATA, true)
                        intent.putExtra(GlobalConstants.KEY_FORM_ID, form.formId)
                        intent.putExtra(GlobalConstants.KEY_JSON_DATA, form.formValues.toString())
                        intent.putExtra(GlobalConstants.LAST_UPLOAD_ERROR, form.lastUploadError)
                        Form.setENCOUNTER_NAME(form.encounterType, form.componentFormUUID)
                        context.startActivity(intent)
                    } else {
                        ToastyWidget.getInstance()
                            .displayError(context, "Other patient is loaded", Toast.LENGTH_SHORT)
                    }

                }

                deleteLayout.setOnClickListener {

                    val dialog = AlertDialog.Builder(context)
                        .setMessage(context.getString(R.string.are_you_sure_to_delete_form))
                        .setTitle(context.getString(R.string.are_you_sure))
                        .setNegativeButton(context.getString(R.string.no), null)
                        .setPositiveButton(context.getString(R.string.yes)) { _, _ ->

                            DataAccess.getInstance().deleteFormByFormID(context, form.formId)
                            DataAccess.getInstance()
                                .decreaseEncounterFilledCount(
                                    context,
                                    Global.USERNAME,
                                    form.getEncounterType(),
                                    form.getFormId(),
                                    form.workflowUUID,
                                    form.componentFormUUID,
                                    Global.HYRDA_CURRENT_URL
                                )
                            if (form.encounterType.equals(ParamNames.ENCOUNTER_TYPE_CREATE_PATIENT)) {
                                DataAccess.getInstance()
                                    .deleteOfflinePatient(context, form.identifier)
                            }
                            formsList.remove(form)
                            notifyDataSetChanged()
                        }
                    dialog.show()

                }
            }
        }

    }

}