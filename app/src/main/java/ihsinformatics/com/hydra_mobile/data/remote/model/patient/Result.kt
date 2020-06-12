package ihsinformatics.com.hydra_mobile.data.remote.model.patient


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("auditInfo")
    val auditInfo: AuditInfo,
    @SerializedName("display")
    val display: String,
    @SerializedName("identifiers")
    val identifiers: List<Identifier>,
    @SerializedName("person")
    val person: Person,
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("voided")
    val voided: Boolean
)