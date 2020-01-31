package ihsinformatics.com.hydra_mobile.data.remote.service

import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.*
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface FormResultApiService {

    @Headers("Accept: application/json")

    @GET("hydra/form")
    fun getFormsResult(): Call<String>

    @GET("hydra/form")
    fun fetchFormsResult(): Observable<MyFormResultApiResponse>
}