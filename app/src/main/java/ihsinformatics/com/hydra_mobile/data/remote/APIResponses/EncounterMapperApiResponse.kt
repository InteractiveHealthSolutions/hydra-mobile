package ihsinformatics.com.hydra_mobile.data.remote.APIResponses


import com.google.gson.annotations.SerializedName
import com.ihsinformatics.dynamicformsgenerator.data.pojos.EncounterMapper

data class EncounterMapperApiResponse(
    @SerializedName("results") val encounterMapper: ArrayList<EncounterMapper>)