package ihsinformatics.com.hydra_mobile.data.remote.model.user


import com.google.gson.annotations.SerializedName

data class AuditInfo(
    @SerializedName("changedBy")
    val changedBy: ChangedBy,
    @SerializedName("creator")
    val creator: Creator,
    @SerializedName("dateChanged")
    val dateChanged: String,
    @SerializedName("dateCreated")
    val dateCreated: String
)