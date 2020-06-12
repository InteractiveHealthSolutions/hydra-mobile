package ihsinformatics.com.hydra_mobile.data.remote.APIResponses


import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestType

data class AttributesApiResponse (

    @SerializedName("results")
    val attributes: ArrayList<AttributeTypeResponse>

)


data class AttributeTypeResponse(
    val datatypeConfig: String,
    val datatypeClassname: String,
    val display: String,
    val labTestType: LabTestType,
    val name: String,
    val uuid: String
                                )