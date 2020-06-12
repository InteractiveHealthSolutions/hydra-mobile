package ihsinformatics.com.hydra_mobile.data.remote.APIResponses


import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.Encounter

data class EncountersApiResponse (

    @SerializedName("results")
    val encounters: ArrayList<Encounter>

)