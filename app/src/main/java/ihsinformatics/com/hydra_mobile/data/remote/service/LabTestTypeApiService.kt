package ihsinformatics.com.hydra_mobile.data.remote.service

import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.LabTestTypeApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface LabTestTypeApiService {

    @Headers("Accept: application/json")

    @GET("commonlab/labtesttype")
    fun getLabTestType( @Query("v") representation: String): Call<LabTestTypeApiResponse>

}