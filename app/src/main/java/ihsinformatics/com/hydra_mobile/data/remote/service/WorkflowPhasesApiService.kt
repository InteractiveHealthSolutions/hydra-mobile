package ihsinformatics.com.hydra_mobile.data.remote.service

import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesApiResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface WorkflowPhasesApiService {

    @Headers("Accept: application/json")
    @GET("hydra/workflowphases")
    fun getWorkflowPhases(@Query("v") representation: String): Call<WorkflowPhasesApiResponse>

    @GET("hydra/workflowphases")
    fun fetchWorkflowPhases(@Query("v") representation: String): Observable<WorkflowPhasesApiResponse>
}