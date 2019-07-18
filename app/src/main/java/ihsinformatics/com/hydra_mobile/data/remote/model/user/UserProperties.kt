package ihsinformatics.com.hydra_mobile.data.remote.model.user


import com.google.gson.annotations.SerializedName

data class UserProperties(
    @SerializedName("loginAttempts")
    val loginAttempts: String,
    @SerializedName("patientGraphConcepts")
    val patientGraphConcepts: String
)