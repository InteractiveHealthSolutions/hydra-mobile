package ihsinformatics.com.hydra_mobile.data.remote.model.patient


import com.google.gson.annotations.SerializedName

data class PreferredName(
    @SerializedName("display")
    val display: String,
    @SerializedName("familyName")
    val familyName: String,
    @SerializedName("familyName2")
    val familyName2: Any,
    @SerializedName("givenName")
    val givenName: String,
    @SerializedName("middleName")
    val middleName: Any,
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("voided")
    val voided: Boolean
)