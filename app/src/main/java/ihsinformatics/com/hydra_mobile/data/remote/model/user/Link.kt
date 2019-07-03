package ihsinformatics.com.hydra_mobile.data.remote.model.user


import com.google.gson.annotations.SerializedName

data class Link(
    @SerializedName("rel")
    val rel: String,
    @SerializedName("uri")
    val uri: String
)