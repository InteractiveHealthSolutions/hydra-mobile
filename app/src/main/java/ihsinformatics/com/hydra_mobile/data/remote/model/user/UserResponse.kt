package ihsinformatics.com.hydra_mobile.data.remote.model.user


import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("results")
    val userList: List<Result>
)