package ihsinformatics.com.hydra_mobile.data.remote.model.patient


import com.google.gson.annotations.SerializedName

data class Attribute(
    @SerializedName("attributeType")
    val attributeType: AttributeType,
    @SerializedName("display")
    val display: String,
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("value")
    val value: Value,
    @SerializedName("voided")
    val voided: Boolean
)