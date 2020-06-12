package ihsinformatics.com.hydra_mobile.data.remote.model.patient


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("display")
    val display: String,
    @SerializedName("uuid")
    val uuid: String
)