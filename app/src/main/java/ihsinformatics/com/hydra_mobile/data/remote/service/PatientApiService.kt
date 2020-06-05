package ihsinformatics.com.hydra_mobile.data.remote.service

import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.EncounterMapperApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.EncountersApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.ReportEncountersApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.PatientApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.user.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface PatientApiService {


    @Headers("Accept: application/json")
    @GET("patient")
    fun getPatientByIdentifier(@Query("identifier") identifier : String): Call<PatientApiResponse>


    @Headers("Accept: application/json")
    @GET("patient")
    fun getPatientByQuery(@Query("q") queryString : String, @Query("v") representation: String
    ): Call<PatientApiResponse>


    @GET("encounter")
    fun getEncountersByPatientUUID(
        @Query("patient") patient: String, @Query("v") representation: String
    ): Call<EncountersApiResponse>

    @GET("encounter")
    fun getEncountersOfPatient(
        @Query("q") queryString: String, @Query("v") representation: String
    ): Call<ReportEncountersApiResponse>


    @GET("qxr/encounterMapper")
    fun getXRayOrderFormByPatientIdentifier(
        @Query("q") identifier: String, @Query("v") representation: String
    ): Call<EncounterMapperApiResponse>
}

