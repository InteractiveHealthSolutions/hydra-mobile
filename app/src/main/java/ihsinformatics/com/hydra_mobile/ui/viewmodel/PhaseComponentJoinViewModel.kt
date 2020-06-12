package ihsinformatics.com.hydra_mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Component
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.ComponentForm
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.PhaseComponentMap
import ihsinformatics.com.hydra_mobile.data.repository.ComponentRepository
import ihsinformatics.com.hydra_mobile.data.repository.PhaseComponentMapRepository


class PhaseComponentJoinViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: PhaseComponentMapRepository = PhaseComponentMapRepository(application)

    private var componentRepository: ComponentRepository = ComponentRepository(application)

    fun insertPhaseComponentMap(phasesComponentMap: PhaseComponentMap) {
        repository.insert(phasesComponentMap)
    }

    fun updatePhaseComponentMap(phasesComponentMap: PhaseComponentMap) {
        repository.updatePhaseComponentMap(phasesComponentMap)
    }

    fun getPhaseComponentMapByPhasesUUID(phaseID:String):List<PhaseComponentMap>
    {
        return repository.getPhaseComponentMapListByPhaseId(phaseID)
    }

    fun getComponentsByPhaseandWorkflow(phaseUUID:String,workflowUUID:String):List<PhaseComponentMap>
    {
        return repository.getComponentsByPhaseandWorkflow(phaseUUID,workflowUUID)
    }

    fun getComponentsByWorkflow(workflowUUID:String):List<PhaseComponentMap>
    {
        return repository.getComponentsByWorkflow(workflowUUID)
    }

    fun getComponentByComponentUUID(componentUUID:String):Component
    {
        return componentRepository.getComponentByUUID(componentUUID)
    }

}