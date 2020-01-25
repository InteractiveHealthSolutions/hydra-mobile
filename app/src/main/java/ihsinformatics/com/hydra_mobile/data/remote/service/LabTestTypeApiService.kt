package ihsinformatics.com.hydra_mobile.data.remote.service

import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.LabTestTypeApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers


interface LabTestTypeApiService {

    @Headers("Accept: application/json")

    @GET("commonlab/labtesttype")
    fun getLabTestType(): Call<LabTestTypeApiResponse>

}