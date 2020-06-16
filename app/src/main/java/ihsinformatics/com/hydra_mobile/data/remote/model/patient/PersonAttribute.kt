package ihsinformatics.com.hydra_mobile.data.remote.model.patient


import com.google.gson.annotations.SerializedName

data class PersonAttribute(
    @SerializedName("attributeType")
    val attributeType: PersonAttributeType,
    @SerializedName("display")
    val display: String,
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("value")
    val value: String
)