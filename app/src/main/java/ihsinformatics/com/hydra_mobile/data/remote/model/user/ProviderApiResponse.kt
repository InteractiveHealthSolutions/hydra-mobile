package ihsinformatics.com.hydra_mobile.data.remote.model.user


import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestOrder

data class ProviderApiResponse (

    @SerializedName("results")
    val providerResult: ArrayList<Provider>

)