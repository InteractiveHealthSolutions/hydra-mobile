package ihsinformatics.com.hydra_mobile.data.remote.model.user


import com.google.gson.annotations.SerializedName

data class AllRole(
    @SerializedName("description")
    val description: String,
    @SerializedName("display")
    val display: String,
    @SerializedName("inheritedRoles")
    val inheritedRoles: List<Any>,
    @SerializedName("links")
    val links: List<Link>,
    @SerializedName("name")
    val name: String,
    @SerializedName("privileges")
    val privileges: List<Any>,
    @SerializedName("resourceVersion")
    val resourceVersion: String,
    @SerializedName("retired")
    val retired: Boolean,
    @SerializedName("uuid")
    val uuid: String
)