package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab


import com.google.gson.annotations.SerializedName

data class TestSampleApiResponse (

    @SerializedName("results")
    val testSamples: ArrayList<TestSample>

)