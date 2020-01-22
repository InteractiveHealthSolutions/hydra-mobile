package ihsinformatics.com.hydra_mobile.data.remote.service

import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.AttributesApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.CommonLabApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.Concept
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.EncountersApiResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface CommonLabApiService {


    @Headers("Accept: application/json")
    @GET("commonlab/labtestorder")
    fun getLabTestOrderByPatientUUID(
        @Query("patient") patientUUID: String, @Query("v") representation: String
    ): Call<CommonLabApiResponse>

//    @GET("commonlab/labtestsample")
//    fun getTestSampleByLabTestUUID(
//        @Query("labtest") labtest: String, @Query("v") representation: String
//    ): Call<TestSampleApiResponse>


    @GET("encounter")
    fun getEncountersByPatientUUID(
        @Query("patient") patient: String, @Query("v") representation: String
    ): Call<EncountersApiResponse>


    @GET("commonlab/labtestattribute")
    fun getLabTestAttribute(
        @Query("testOrderId") testOrderId: String, @Query("v") representation: String
    ): Call<AttributesApiResponse>

    @GET("concept/{uuid}")
    fun getConcepts(@Path(value = "uuid") uuid :String): Call<Concept>

    @POST("commonlab/labtestorder")
    fun addLabTestOrder(@Body body: RequestBody): Call<CommonLabApiResponse>

}

