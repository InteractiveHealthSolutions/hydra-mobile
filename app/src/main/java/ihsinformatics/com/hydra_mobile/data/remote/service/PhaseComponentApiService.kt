package ihsinformatics.com.hydra_mobile.data.remote.service

import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.PhaseComponentApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesApiResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface PhaseComponentApiService {

    @Headers("Accept: application/json")
    @GET("hydra/phasecomponent")
    fun getPhaseComponent(@Query("v") representation: String): Call<PhaseComponentApiResponse>

    @GET("hydra/phasecomponent")
    fun fetchPhaseComponent(@Query("v") representation: String): Observable<PhaseComponentApiResponse>
}