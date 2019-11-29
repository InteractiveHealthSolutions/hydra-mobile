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
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesMap
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

    fun getAllWorkFlows(): LiveData<List<WorkFlow>> {
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


    fun getRemoteWorkFlowData() {

        RequestManager(
            context, sessionManager.getUsername(),
            sessionManager.getPassword()
        ).getWorkflow(
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
                        Log.e("WorkflowLoading", "completed")
                    } catch (e: Exception) {

                    }
                }

                override fun onFailure(t: Throwable) {

                }
            })
    }

}