package ihsinformatics.com.hydra_mobile.data.remote.model.patient


import com.google.gson.annotations.SerializedName

data class Person(
    @SerializedName("age")
    val age: Int,
    @SerializedName("dead")
    val dead: Boolean,
    @SerializedName("display")
    val display: String,
    @SerializedName("gender")
    val gender: String
)