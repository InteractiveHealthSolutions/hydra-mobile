package ihsinformatics.com.hydra_mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesMap
import ihsinformatics.com.hydra_mobile.data.repository.WorkflowPhasesRepository


class WorkflowPhasesMapViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: WorkflowPhasesRepository = WorkflowPhasesRepository(application)


    fun update(workflowPhasesMap: WorkflowPhasesMap) {
        repository.updateWorkflowPhases(workflowPhasesMap)
    }


    fun getPhasesByWorkFlowUUID(workflowUUID:String):List<WorkflowPhasesMap>
    {
        return repository.getPhasesByWorkFlowUUID(workflowUUID)
    }

}