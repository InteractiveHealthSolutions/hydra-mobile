package ihsinformatics.com.hydra_mobile.data.remote.model.user


import com.google.gson.annotations.SerializedName

data class ChangedBy(
    @SerializedName("display")
    val display: String,
    @SerializedName("links")
    val links: List<Link>,
    @SerializedName("uuid")
    val uuid: String
)