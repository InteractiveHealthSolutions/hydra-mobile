package ihsinformatics.com.hydra_mobile.data.remote.service

import ihsinformatics.com.hydra_mobile.data.remote.model.user.ProviderApiResponse
import retrofit2.Call
import retrofit2.http.*


interface ProviderApiService {


    @Headers("Accept: application/json")
    @GET("provider")
    fun getProviderData(@Query("q") q: String): Call<ProviderApiResponse>
}

