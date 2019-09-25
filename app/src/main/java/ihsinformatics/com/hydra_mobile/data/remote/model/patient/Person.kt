package ihsinformatics.com.hydra_mobile.data.remote.model.patient


import com.google.gson.annotations.SerializedName

data class Person(
    @SerializedName("addresses")
    val addresses: List<Addresse>,
    @SerializedName("age")
    val age: Int,
    @SerializedName("attributes")
    val attributes: List<Attribute>,
    @SerializedName("auditInfo")
    val auditInfo: AuditInfo,
    @SerializedName("birthdate")
    val birthdate: String,
    @SerializedName("birthdateEstimated")
    val birthdateEstimated: Boolean,
    @SerializedName("birthtime")
    val birthtime: Any,
    @SerializedName("causeOfDeath")
    val causeOfDeath: Any,
    @SerializedName("dead")
    val dead: Boolean,
    @SerializedName("deathDate")
    val deathDate: Any,
    @SerializedName("deathdateEstimated")
    val deathdateEstimated: Boolean,
    @SerializedName("display")
    val display: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("names")
    val names: List<Name>,
    @SerializedName("preferredAddress")
    val preferredAddress: PreferredAddress,
    @SerializedName("preferredName")
    val preferredName: PreferredName,
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("voided")
    val voided: Boolean
)