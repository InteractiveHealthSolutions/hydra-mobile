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

    fun getComponentByPhasesUUID(phaseID:String):List<PhaseComponentMap>
    {
        return repository.getComponentListByPhaseUUID(phaseID)
    }

    fun getComponentByComponentUUID(componentUUID:String):Component
    {
        return componentRepository.getComponentByUUID(componentUUID)
    }

    fun getFormsByPhaseUUID(phaseID:String){
        var componentList=repository.getComponentListByPhaseUUID(phaseID)
        var componentFormList:List<ComponentForm>
        componentFormList = ArrayList<ComponentForm>()
        var requiredList:ArrayList<ArrayList<ComponentForm>>

        requiredList=ArrayList<ArrayList<ComponentForm>>()

        for(i in 0 until componentList.size)
        {
            componentFormList=componentRepository.getFormListByComponentUUID(componentList.get(i).componentUUID)
            requiredList.add(componentFormList as java.util.ArrayList<ComponentForm>)

        }
    }
}