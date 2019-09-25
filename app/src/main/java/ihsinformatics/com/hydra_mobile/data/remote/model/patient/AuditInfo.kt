package ihsinformatics.com.hydra_mobile.data.remote.model.patient


import com.google.gson.annotations.SerializedName

data class AuditInfo(
    @SerializedName("changedBy")
    val changedBy: Any,
    @SerializedName("creator")
    val creator: Creator,
    @SerializedName("dateChanged")
    val dateChanged: Any,
    @SerializedName("dateCreated")
    val dateCreated: String
)