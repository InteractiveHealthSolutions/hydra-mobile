package ihsinformatics.com.hydra_mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Component
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.PhasesComponentJoin
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesMap
import ihsinformatics.com.hydra_mobile.data.repository.PhaseComponentFormJoinRepository
import ihsinformatics.com.hydra_mobile.data.repository.WorkflowPhasesRepository


class PhaseComponentJoinViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: PhaseComponentFormJoinRepository = PhaseComponentFormJoinRepository(application)


    fun insertPhaseComponentJoin(phasesComponentJoin: PhasesComponentJoin) {
        repository.insert(phasesComponentJoin)
    }

    fun updatePhaseComponentJoin(phasesComponentJoin: PhasesComponentJoin) {
        repository.updatePhaseComponentJoin(phasesComponentJoin)
    }

    fun getPhaseComponentJoinByPhasesID(phaseID:Int):List<PhasesComponentJoin>
    {
        return repository.getPhaseComponentJoinListByPhaseId(phaseID)
    }

    fun getComponentByPhasesID(phaseID:Int):List<Component>
    {
        return repository.getComponentListByPhaseId(phaseID)
    }
}