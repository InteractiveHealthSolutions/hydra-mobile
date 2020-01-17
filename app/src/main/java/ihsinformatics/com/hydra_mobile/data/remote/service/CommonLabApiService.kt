package ihsinformatics.com.hydra_mobile.data.remote.service

import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.CommonLabApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.TestSample
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.TestSampleApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface CommonLabApiService {


    @Headers("Accept: application/json")
    @GET("labtestorder")
    fun getLabTestOrderByPatientUUID(
        @Query("patient") patientUUID: String, @Query("v") representation: String
    ): Call<CommonLabApiResponse>

    @GET("labtestsample")
    fun getTestSampleByLabTestUUID(
        @Query("labtest") labtest: String, @Query("v") representation: String
    ): Call<TestSampleApiResponse>


}

