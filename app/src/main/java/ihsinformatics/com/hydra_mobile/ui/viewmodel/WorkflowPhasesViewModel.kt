package ihsinformatics.com.hydra_mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesMap
import ihsinformatics.com.hydra_mobile.data.repository.WorkflowPhasesRepository


class WorkflowPhasesViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: WorkflowPhasesRepository = WorkflowPhasesRepository(application)


    private var allWorkflowPhases: LiveData<List<WorkflowPhasesMap>> = repository.getAllWorkflowPhases()

    fun insertWorkflowPhases(workflowPhasesMap: WorkflowPhasesMap) {
        repository.insertWorkflowPhases(workflowPhasesMap)
    }

    fun update(workflowPhasesMap: WorkflowPhasesMap) {
        repository.updateWorkflowPhases(workflowPhasesMap)
    }

    fun getAllPWorkflowPhases(): LiveData<List<WorkflowPhasesMap>> {
        return allWorkflowPhases
    }


}