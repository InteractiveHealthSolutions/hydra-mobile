package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab


import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestOrder

data class AttributesApiResponse (

    @SerializedName("results")
    val attributes: ArrayList<LabTestAttribute>

)