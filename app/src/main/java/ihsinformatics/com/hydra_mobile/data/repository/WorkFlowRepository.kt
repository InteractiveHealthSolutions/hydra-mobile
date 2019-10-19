package ihsinformatics.com.hydra_mobile.data.repository


import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.WorkFlowDao
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.WorkflowPhasesMapDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.WorkFlow
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesMap
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.jetbrains.anko.doAsync


class WorkFlowRepository(context: Context) {
    private var workFlowDao: WorkFlowDao
    private var workFlowPhasesMapDao: WorkflowPhasesMapDao
    private var context: Context

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

}