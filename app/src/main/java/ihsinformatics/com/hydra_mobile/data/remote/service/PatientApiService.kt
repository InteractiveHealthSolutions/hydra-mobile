package ihsinformatics.com.hydra_mobile.data.remote.service

import ihsinformatics.com.hydra_mobile.data.remote.model.patient.PatientApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.user.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface PatientApiService {


    @Headers("Accept: application/json")
    @GET("patient")
    fun getPatientByIdentifier(@Query("identifier") identifier : String, @Query("v") representation: String
    ): Call<PatientApiResponse>


    @Headers("Accept: application/json")
    @GET("patient")
    fun getPatientByQuery(@Query("q") queryString : String, @Query("v") representation: String
    ): Call<PatientApiResponse>

}

