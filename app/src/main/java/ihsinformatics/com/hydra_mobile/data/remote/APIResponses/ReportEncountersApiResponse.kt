package ihsinformatics.com.hydra_mobile.data.remote.APIResponses


import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.data.remote.model.history.Encounters

data class ReportEncountersApiResponse(

    @SerializedName("results")
    val encounters: ArrayList<Encounters>

                                      )