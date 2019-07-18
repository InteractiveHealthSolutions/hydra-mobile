package ihsinformatics.com.hydra_mobile.data.remote.model.user


import com.google.gson.annotations.SerializedName

data class Person(
    @SerializedName("age")
    val age: Any,
    @SerializedName("attributes")
    val attributes: List<Any>,
    @SerializedName("birthdate")
    val birthdate: Any,
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
    @SerializedName("links")
    val links: List<Link>,
    @SerializedName("preferredAddress")
    val preferredAddress: Any,
    @SerializedName("preferredName")
    val preferredName: PreferredName,
    @SerializedName("resourceVersion")
    val resourceVersion: String,
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("voided")
    val voided: Boolean
)