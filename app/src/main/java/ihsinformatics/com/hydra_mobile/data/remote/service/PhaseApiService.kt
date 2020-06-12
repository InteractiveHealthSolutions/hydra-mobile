package ihsinformatics.com.hydra_mobile.data.remote.service

import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.PhaseApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkFlowApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesApiResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface PhaseApiService {

    @Headers("Accept: application/json")
    @GET("hydra/phase")
    fun getPhase(@Query("v") representation: String): Call<PhaseApiResponse>

    @GET("hydra/phase")
    fun fetchPhase(@Query("v") representation: String): Observable<PhaseApiResponse>
}