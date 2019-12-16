package ihsinformatics.com.hydra_mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesMap
import ihsinformatics.com.hydra_mobile.data.repository.WorkflowPhasesRepository


class WorkflowPhasesMapViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: WorkflowPhasesRepository = WorkflowPhasesRepository(application)


    //private var allWorkflowPhases: LiveData<List<WorkflowPhasesMap>> = repository.getAllWorkflowPhases()

    fun insertWorkflowPhases(workflowPhasesMap: WorkflowPhasesMap) {
        repository.insertWorkflowPhases(workflowPhasesMap)
    }

    fun update(workflowPhasesMap: WorkflowPhasesMap) {
        repository.updateWorkflowPhases(workflowPhasesMap)
    }

    fun getAllPWorkflowPhases(): List<WorkflowPhasesMap> {
        return repository.getAllWorkflowPhases()
    }

    fun getPhasesByWorkFlowName(workflowString:String):List<WorkflowPhasesMap>
    {
        return repository.getPhasesByWorkFlowName(workflowString)
    }

    fun getPhasesByWorkFlowUUID(workflowUUID:String):List<WorkflowPhasesMap>
    {
        return repository.getPhasesByWorkFlowUUID(workflowUUID)
    }

}