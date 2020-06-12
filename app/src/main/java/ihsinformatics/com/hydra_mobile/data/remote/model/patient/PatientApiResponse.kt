package ihsinformatics.com.hydra_mobile.data.remote.model.patient


import com.google.gson.annotations.SerializedName

data class PatientApiResponse(

    @SerializedName("results")
    val results: List<Patient>
)