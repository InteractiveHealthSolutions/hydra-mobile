package ihsinformatics.com.hydra_mobile.data.remote.APIResponses


import com.google.gson.annotations.SerializedName
import com.ihsinformatics.dynamicformsgenerator.data.database.history.Encounters

data class ReportEncountersApiResponse(
    @SerializedName("results") val encounters: ArrayList<Encounters>)