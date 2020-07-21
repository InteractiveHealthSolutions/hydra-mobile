package ihsinformatics.com.hydra_mobile.data.remote.APIResponses


import com.google.gson.annotations.SerializedName
import com.ihsinformatics.dynamicformsgenerator.data.pojos.FormEncounterMapper

data class ReportEncountersApiResponse(
    @SerializedName("results") val results: ArrayList<FormEncounterMapper>)