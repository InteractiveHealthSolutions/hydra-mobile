package ihsinformatics.com.hydra_mobile.data.remote.service

import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.LabTestTypeApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.LocationApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.SystemSettingApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface LocationApiService {

    @Headers("Accept: application/json")

    @GET("location")
    fun getLocation( @Query("v") representation: String,@Query("limit") limit: Int): Call<LocationApiResponse>

    @GET("systemsetting")
    fun getLocationAndCurrency( @Query("q") query: String,@Query("v") representation: String): Call<SystemSettingApiResponse>

}