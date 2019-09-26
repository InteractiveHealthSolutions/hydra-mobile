package ihsinformatics.com.hydra_mobile.data.repository


import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.WorkflowPhasesMapDao
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesMap
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import org.jetbrains.anko.doAsync


class WorkflowPhasesRepository(context: Context) {

    private lateinit var workflowPhasesMapDao: WorkflowPhasesMapDao
    //private var application: Application
    private var context: Context
    private val sessionManager = SessionManager(context)

    init {
        val database: AppDatabase = AppDatabase.getInstance(
            context.applicationContext
        )!!
        workflowPhasesMapDao = database.getWorkflowPhases()
        this.context = context

    }


    fun insertWorkflowPhases(workflowPhasesMap: WorkflowPhasesMap) {
        doAsync {
            workflowPhasesMapDao.insertWorkflowPhases(workflowPhasesMap)
        }
    }

    fun getRemoteWorkflowData() {
        RequestManager(
            context, sessionManager.getUsername(),
            sessionManager.getPassword()
        ).getWorkflowPhases(
            Constant.REPRESENTATION,
            object :
                RESTCallback {
                override fun <T> onSuccess(o: T) {

                    try {
                        val response = (o as WorkflowPhasesApiResponse)
                        for (i in response.workflowPhasesMap.indices) {
                            //insert into local database
                            insertWorkflowPhases(response.workflowPhasesMap[i])
                        }
                        Log.e("WorkflowLoading", "completed")
                    } catch (e: Exception) {

                    }
                }

                override fun onFailure(t: Throwable) {

                }
            })
    }

    fun updateWorkflowPhases(workflowPhasesMap: WorkflowPhasesMap) {
        doAsync {
            workflowPhasesMapDao.updateWorkflowPhases(workflowPhasesMap)
        }
    }

    fun deleteAllWorkflowPhases() {
        doAsync {
            workflowPhasesMapDao.deleteAllWorkflowPhases()
        }
    }

    fun getAllWorkflowPhases(): LiveData<List<WorkflowPhasesMap>> {
        return workflowPhasesMapDao.getAllWorkflowPhases()
    }

}