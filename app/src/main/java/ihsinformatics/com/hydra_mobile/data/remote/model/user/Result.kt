package ihsinformatics.com.hydra_mobile.data.remote.model.user


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("display")
    val display: String,
    @SerializedName("links")
    val links: List<Link>,
    @SerializedName("allRoles")
    val allRoles: List<AllRole>,
    @SerializedName("auditInfo")
    val auditInfo: AuditInfo,
    @SerializedName("person")
    val person: Person,
    @SerializedName("privileges")
    val privileges: List<Any>,
    @SerializedName("proficientLocales")
    val proficientLocales: List<Any>,
    @SerializedName("resourceVersion")
    val resourceVersion: String,
    @SerializedName("retired")
    val retired: Boolean,
    @SerializedName("roles")
    val roles: List<Role>,
    @SerializedName("systemId")
    val systemId: String,
    @SerializedName("userProperties")
    val userProperties: UserProperties,
    @SerializedName("username")
    val username: String
)