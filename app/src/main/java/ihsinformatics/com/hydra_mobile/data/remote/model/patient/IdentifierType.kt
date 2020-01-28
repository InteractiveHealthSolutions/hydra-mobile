package ihsinformatics.com.hydra_mobile.data.remote.model.patient


import com.google.gson.annotations.SerializedName

data class IdentifierType(
    @SerializedName("display")
    val identifierTypeDisplay: String,
    @SerializedName("uuid")
    val identifierTypeUUID: String
)