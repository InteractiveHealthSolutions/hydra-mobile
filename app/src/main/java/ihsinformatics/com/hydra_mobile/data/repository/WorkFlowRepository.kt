package ihsinformatics.com.hydra_mobile.data.repository


import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.WorkFlowDao
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.WorkflowPhasesMapDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.WorkFlow
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkFlowApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkFlowUserMappingApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesMap
import ihsinformatics.com.hydra_mobile.data.services.manager.RetrofitResponseListener
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.jetbrains.anko.doAsync


class WorkFlowRepository(context: Context) {
    private var workFlowDao: WorkFlowDao
    private var workFlowPhasesMapDao: WorkflowPhasesMapDao
    private var context: Context
    private val sessionManager = SessionManager(context)


    init {
        val database: AppDatabase = AppDatabase.getInstance(
            context.applicationContext
        )!!
        workFlowDao = database.getWorkFlowDao()
        workFlowPhasesMapDao = database.getWorkflowPhases()
        this.context = context
    }


    fun insertWorkFlow(workflow: WorkFlow) {
        doAsync {
            workFlowDao.insertWorkFlow(workflow)
        }
    }

    fun updateWorkFlow(workflow: WorkFlow) {
        doAsync {
            workFlowDao.updateWorkFlow(workflow)
        }
    }

    fun getAllWorkFlows(): List<WorkFlow> {
        return workFlowDao.getAllWorkFlows()

    }

    fun getAllWorkFlowsFromLiveData(): LiveData<List<WorkFlow>> {
        return workFlowDao.getAllWorkFlowsLiveData()
    }

    fun getAllWorkFlowPhasesWithLiveData(): LiveData<List<WorkflowPhasesMap>> {
        return workFlowPhasesMapDao.getAllWorkflowPhasesWithLiveData()
    }

    suspend fun getAllWorkFlowPhases(): List<WorkflowPhasesMap> {
        var workflowList = GlobalScope.async {
            workFlowPhasesMapDao.getAllWorkflowPhases()
        }
        return workflowList.await()
    }


    fun getWorkflowPhasesByName(name: String): List<WorkflowPhasesMap> {
        return workFlowPhasesMapDao.getWorkflowPhasesByName(name)
    }

    fun deleteAllWorkflow() {
        doAsync {
            workFlowDao.deleteAllWorkflow()
        }
    }


    fun getRemoteAllWorkFlowData(retrofitResponseListener: RetrofitResponseListener) {

        RequestManager(
            context, sessionManager.getUsername(),
            sessionManager.getPassword()
        ).getAllWorkFlow(
            Constant.REPRESENTATION,
            object :
                RESTCallback {
                override fun <T> onSuccess(o: T) {

                    try {
                        val response = (o as WorkFlowApiResponse)
                        for (i in response.workflow.indices) {
                            //insert into local database
                            insertWorkFlow(response.workflow[i])
                        }
                        retrofitResponseListener.onSuccess()
                        Log.e("WorkflowLoading", "completed")
                    } catch (e: Exception) {
                        retrofitResponseListener.onFailure()
                    }
                }

                override fun onFailure(t: Throwable) {
                    retrofitResponseListener.onFailure()
                }
            })
    }


    fun getRemoteWorkFlowDataByUserMapping(retrofitResponseListener: RetrofitResponseListener) {

        RequestManager(
            context, sessionManager.getUsername(),
            sessionManager.getPassword()
        ).getWorkFlowByUserMapping(
            Constant.REPRESENTATION,
            sessionManager.getUserUUID(),
            object :
                RESTCallback {
                override fun <T> onSuccess(o: T) {

                    try {
                        val response = (o as WorkFlowUserMappingApiResponse)
                        if (response.workflowMapping.size > 0) {
                            for (i in response.workflowMapping.indices) {
                                //insert into local database
                                insertWorkFlow(response.workflowMapping[i].workflow)
                            }
                        }else
                        {
                            getRemoteAllWorkFlowData(retrofitResponseListener)
                        }
                        retrofitResponseListener.onSuccess()
                        Log.e("WorkflowLoading", "completed")
                    } catch (e: Exception) {
                        retrofitResponseListener.onFailure()
                    }
                }

                override fun onFailure(t: Throwable) {
                    retrofitResponseListener.onFailure()
                }
            })
    }
}