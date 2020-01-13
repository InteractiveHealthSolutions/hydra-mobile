package ihsinformatics.com.hydra_mobile.data.remote.model.patient


import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.Patient

data class PatientApiResponse(

    @SerializedName("results")
    val labTests: List<Patient>
)