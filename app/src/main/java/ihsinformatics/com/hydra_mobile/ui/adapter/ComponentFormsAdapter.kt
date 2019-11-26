package ihsinformatics.com.hydra_mobile.ui.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ihsinformatics.dynamicformsgenerator.PatientInfoFetcher
import com.ihsinformatics.dynamicformsgenerator.data.DataProvider
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames
import com.ihsinformatics.dynamicformsgenerator.screens.Form
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Forms
import ihsinformatics.com.hydra_mobile.ui.activity.FormsActivity
import ihsinformatics.com.hydra_mobile.ui.helper.GlideApp
import androidx.core.content.ContextCompat.startActivity
import com.ihsinformatics.dynamicformsgenerator.network.pojos.PatientData
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import org.json.JSONException


internal class FormsListDataAdapter(private val itemModels: List<Forms>, context: Context) :
    RecyclerView.Adapter<FormsListDataAdapter.SingleItemRowHolder>() {

    var clickedFormID = -1
    var context: Context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleItemRowHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_forms_card, null)
        return SingleItemRowHolder(v)
    }

    override fun onBindViewHolder(holder: SingleItemRowHolder, position: Int) {
        val formModel = itemModels!![position]

        var imageId = R.drawable.ic_form
        imageId = when (formModel.encounterType) {
            ParamNames.ENCOUNTER_TYPE_ADULT_TX_INITIATION, ParamNames.ENCOUNTER_TYPE_CHILD_TX_INITIATION -> R.drawable.ic_treatment_initiate
            ParamNames.ENCOUNTER_TYPE_ADULT_SCREENING, ParamNames.ENCOUNTER_TYPE_CHILD_SCREENING -> R.drawable.ic_screening_lung
            ParamNames.ENCOUNTER_TYPE_PATIENT_INFO -> R.drawable.ic_patient_info
            ParamNames.ENCOUNTER_TYPE_CREATE_PATIENT -> R.drawable.ic_add_user
            ParamNames.ENCOUNTER_TYPE_CONTACT_REGISTRY -> R.drawable.ic_network
            ParamNames.ENCOUNTER_TYPE_ADULT_CLINICAL_EVALUATION, ParamNames.ENCOUNTER_TYPE_CHILD_CLINICAL_EVALUATION -> R.drawable.ic_clinic_evaluation
            ParamNames.ENCOUNTER_TYPE_ADVERSE_EVENT -> R.drawable.ic_adverse_effect


            else -> R.drawable.ic_patient_information
        }

        holder.tvTitle.text = formModel.encounterType
        clickedFormID = formModel.id
        GlideApp.with(context)
            .load(imageId)
            .into(holder.imgForm);
    }

    override fun getItemCount(): Int {
        return itemModels?.size ?: 0
    }

    inner class SingleItemRowHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvTitle: TextView = itemView.findViewById(R.id.tv_form_name)
        var imgForm: ImageView = itemView.findViewById(R.id.img_form_image)

        init {
            itemView.setOnClickListener {
                Constant.formName = tvTitle.text.toString()
                Constant.formID = clickedFormID
                if (DataProvider.directOpenableForms.contains(Constant.formName)) {
                    Form.setENCOUNTER_NAME(Constant.formName)
                    context.startActivity(Intent(context, Form::class.java))
                } else {


                    if (Global.patientData == null) {
//                        PatientInfoFetcher.init(Constant.formName, PatientInfoFetcher.REQUEST_TYPE.FETCH_INFO)
//                        context.startActivity(Intent(context, PatientInfoFetcher::class.java))
                        ToastyWidget.getInstance().displayError(
                            context,
                            context.resources.getString(R.string.patient_not_loaded),
                            Toast.LENGTH_SHORT
                        )

                    } else {
                        var age = Global.patientData.patient.age
                        if (null != age && age > 17 && (Constant.formName.equals(ParamNames.ENCOUNTER_TYPE_CHILD_CLINICAL_EVALUATION) || Constant.formName.equals(
                                ParamNames.ENCOUNTER_TYPE_CHILD_SCREENING
                            ) || Constant.formName.equals(ParamNames.ENCOUNTER_TYPE_CHILD_TX_INITIATION))
                        ) {
                            ToastyWidget.getInstance()
                                .displayError(context, "You don't have access to child form", Toast.LENGTH_SHORT)
                        } else if (null != age && age < 18 && (Constant.formName.equals(ParamNames.ENCOUNTER_TYPE_ADULT_CLINICAL_EVALUATION) || Constant.formName.equals(
                                ParamNames.ENCOUNTER_TYPE_ADULT_SCREENING
                            ) || Constant.formName.equals(ParamNames.ENCOUNTER_TYPE_ADULT_TX_INITIATION))
                        ) {
                            ToastyWidget.getInstance()
                                .displayError(context, "You don't have access to adult form", Toast.LENGTH_SHORT)
                        } else {
                            Form.setENCOUNTER_NAME(Constant.formName)
                            context.startActivity(Intent(context, Form::class.java))
                        }
                    }
                }
            }
        }


    }
}
