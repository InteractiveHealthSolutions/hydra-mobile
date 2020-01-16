package ihsinformatics.com.hydra_mobile.data.remote.model.patient


import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestOrder

data class CommonLabApiResponse (

    @SerializedName("results")
    val labTests: ArrayList<LabTestOrder>

)