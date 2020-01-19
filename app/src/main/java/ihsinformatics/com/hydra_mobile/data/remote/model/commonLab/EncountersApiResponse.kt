package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab


import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestOrder

data class EncountersApiResponse (

    @SerializedName("results")
    val encounters: ArrayList<Encounter>

)