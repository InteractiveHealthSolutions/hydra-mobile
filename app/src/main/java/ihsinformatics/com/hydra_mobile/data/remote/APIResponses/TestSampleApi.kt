package ihsinformatics.com.hydra_mobile.data.remote.APIResponses


import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.TestSample

data class TestSampleApi(

    @SerializedName("results")
    val testSamples: ArrayList<TestSample>

                        )