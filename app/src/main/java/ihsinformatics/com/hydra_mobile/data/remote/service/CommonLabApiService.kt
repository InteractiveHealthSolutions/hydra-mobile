package ihsinformatics.com.hydra_mobile.data.remote.service

import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.*
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface CommonLabApiService {


    @Headers("Accept: application/json")
    @GET("commonlab/labtestorder")
    fun getLabTestOrderByPatientUUID(
        @Query("patient") patientUUID: String, @Query("v") representation: String
    ): Call<CommonLabApiResponse>


    @GET("commonlab/labtestattributetype")
    fun getLabTestAttributeType(
        @Query("testTypeUuid") testTypeUuid: String, @Query("v") representation: String
    ): Call<AttributesApiResponse>

    @GET("commonlab/labtestattribute")
    fun getLabTestAttribute(
        @Query("testOrderId") testOrderId: String, @Query("v") representation: String
                               ): Call<AttributeResponse>

    @GET("concept/{uuid}")
    fun getConcepts(@Path(value = "uuid") uuid :String): Call<Concept>

    @POST("commonlab/labtestorder")
    fun addLabTestOrder(@Body body: RequestBody): Call<CommonLabApiResponse>

    @POST("commonlab/labtestsample")
    fun addTestSample(@Body body: RequestBody):Call<TestSampleApi>
}

