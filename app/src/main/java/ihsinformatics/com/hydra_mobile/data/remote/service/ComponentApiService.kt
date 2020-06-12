package ihsinformatics.com.hydra_mobile.data.remote.service

import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.ComponentApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.PhaseApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkFlowApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesApiResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface ComponentApiService {

    @Headers("Accept: application/json")
//    @GET("hydra/component")
//    fun getComponents(@Query("v") representation: String): Call<ComponentApiResponse>
//
//    @GET("hydra/component")
//    fun fetchComponents(@Query("v") representation: String): Observable<ComponentApiResponse>

    @GET("hydra/component")
    fun getComponents(): Call<ComponentApiResponse>

    @GET("hydra/component")
    fun fetchComponents(): Observable<ComponentApiResponse>
}