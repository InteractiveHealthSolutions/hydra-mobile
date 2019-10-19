package ihsinformatics.com.hydra_mobile.data.remote.model.patient


import com.google.gson.annotations.SerializedName

data class IdentifierType(
    @SerializedName("display")
    val display: String,
    @SerializedName("uuid")
    val uuid: String
)