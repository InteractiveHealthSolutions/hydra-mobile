package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab

import com.google.gson.annotations.SerializedName

data class TestOrderModel(
    @SerializedName("display")
    val display: String,

    @SerializedName("labReferenceNumber")
    val labReferenceNumber: String,

    @SerializedName("labTestType")
    val labTestType: LabTestType
)


class LabTestType(

    @SerializedName("uuid")
    val uuid: String,

    @SerializedName("display")
    val display: String,

    @SerializedName("testGroup")
    val testGroup: String,

    @SerializedName("shortName")
    val shortName: String,

    @SerializedName("requiresSpecimen")
    val requiresSpecimen: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String

)