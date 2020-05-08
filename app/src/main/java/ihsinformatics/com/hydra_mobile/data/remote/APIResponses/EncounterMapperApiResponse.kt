package ihsinformatics.com.hydra_mobile.data.remote.APIResponses


import com.google.gson.annotations.SerializedName
import com.ihsinformatics.dynamicformsgenerator.data.database.EncounterMapper
import com.ihsinformatics.dynamicformsgenerator.data.database.history.Encounters

data class EncounterMapperApiResponse(
    @SerializedName("results") val encounterMapper: ArrayList<EncounterMapper>)