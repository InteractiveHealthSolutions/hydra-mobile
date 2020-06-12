package ihsinformatics.com.hydra_mobile.data.remote.APIResponses


import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.Attribute

data class AttributeResponse (

    @SerializedName("results")
    val attributes: ArrayList<Attribute>

)
