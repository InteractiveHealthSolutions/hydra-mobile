package ihsinformatics.com.hydra_mobile.data.remote.model.patient


import com.google.gson.annotations.SerializedName

data class Identifier(
    @SerializedName("display")
    val display: String,
    @SerializedName("identifier")
    val identifier: String,
    @SerializedName("identifierType")
    val identifierType: IdentifierType,
    @SerializedName("location")
    val location: Location,
    @SerializedName("preferred")
    val preferred: Boolean,
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("voided")
    val voided: Boolean
)