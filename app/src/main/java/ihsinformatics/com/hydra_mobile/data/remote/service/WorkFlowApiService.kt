package ihsinformatics.com.hydra_mobile.data.remote.service

import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkFlowApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesApiResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface WorkFlowApiService {

    @Headers("Accept: application/json")
    @GET("hydra/workflow")
    fun getWorkFlow(@Query("v") representation: String): Call<WorkFlowApiResponse>

    @GET("hydra/workflow")
    fun fetchWorkFlow(@Query("v") representation: String): Observable<WorkFlowApiResponse>
}