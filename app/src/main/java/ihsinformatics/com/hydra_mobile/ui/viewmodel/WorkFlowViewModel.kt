package ihsinformatics.com.hydra_mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.WorkFlow
import ihsinformatics.com.hydra_mobile.data.repository.WorkFlowRepository


class WorkFlowViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: WorkFlowRepository = WorkFlowRepository(application)

    fun update(workFlow: WorkFlow) {
        repository.updateWorkFlow(workFlow)
    }

    fun getAllWorkflowLiveData(): LiveData<List<WorkFlow>> {
        return repository.getAllWorkFlowsFromLiveData()
    }

    fun getAllWorkflow(): List<WorkFlow> {
        return repository.getAllWorkFlows()
    }



}