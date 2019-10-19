package ihsinformatics.com.hydra_mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.WorkFlow
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesMap
import ihsinformatics.com.hydra_mobile.data.repository.WorkFlowRepository
import ihsinformatics.com.hydra_mobile.data.repository.WorkflowPhasesRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


class WorkFlowViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: WorkFlowRepository = WorkFlowRepository(application)

    fun insertWorkflow(workFlow: WorkFlow) {
        repository.insertWorkFlow(workFlow)
    }

    fun update(workFlow: WorkFlow) {
        repository.updateWorkFlow(workFlow)
    }

    fun getAllPWorkflow(): LiveData<List<WorkFlow>> {
        return repository.getAllWorkFlows()
    }


}