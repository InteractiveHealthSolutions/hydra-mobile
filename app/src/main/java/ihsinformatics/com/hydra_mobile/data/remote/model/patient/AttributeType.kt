package ihsinformatics.com.hydra_mobile.data.remote.model.patient


import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestType

data class AttributeType(
    @SerializedName("display")
    val display: String,
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("labTestType")
    val labTestType: LabTestType

    )