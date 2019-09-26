package ihsinformatics.com.hydra_mobile.data.repository

import androidx.annotation.NonNull
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.common.NetworkBoundResource
import ihsinformatics.com.hydra_mobile.common.Resource
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.WorkflowPhasesMapDao
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesMap
import ihsinformatics.com.hydra_mobile.data.remote.service.WorkflowPhasesApiService
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Singleton


@Singleton
class LoadDataRepository(
    workflowPhasesMapDao: WorkflowPhasesMapDao,
    workflowPhasesApiService: WorkflowPhasesApiService
) {

    private var workflowPhasesMapDao: WorkflowPhasesMapDao = workflowPhasesMapDao
    private var workflowPhasesApiService: WorkflowPhasesApiService = workflowPhasesApiService

    public fun loadWorkflow(): Observable<Resource<List<WorkflowPhasesMap>>> {
        return object : NetworkBoundResource<List<WorkflowPhasesMap>, WorkflowPhasesApiResponse>() {

            override fun saveCallResult(@NonNull item: WorkflowPhasesApiResponse) {

                for (i in item.workflowPhasesMap.indices) {
                    workflowPhasesMapDao.insertWorkflowPhases(item.workflowPhasesMap[i])
                }
            }

            override fun shouldFetch(): Boolean {
                return true
            }

            @NonNull
            override fun loadFromDb(): Flowable<List<WorkflowPhasesMap>> {
                return Flowable.empty<List<WorkflowPhasesMap>>()
            }

            @NonNull
            override fun createCall(): Observable<Resource<WorkflowPhasesApiResponse>> {
                return workflowPhasesApiService.fetchWorkflowPhases(Constant.REPRESENTATION)
                    .flatMap { workflowApiResponse ->
                        Observable.just(
                            if (workflowApiResponse == null)
                                Resource.error("", WorkflowPhasesApiResponse())
                            else
                                Resource.success(workflowApiResponse)
                        )
                    }
            }
        }.asObservable

    }




}