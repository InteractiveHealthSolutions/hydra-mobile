package ihsinformatics.com.hydra_mobile.ui.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.PhaseComponentDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.*
import ihsinformatics.com.hydra_mobile.data.repository.ComponentRepository
import ihsinformatics.com.hydra_mobile.data.repository.PhaseComponentFormJoinRepository
import ihsinformatics.com.hydra_mobile.data.repository.PhaseRepository

class PhasesViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: PhaseRepository = PhaseRepository(application)

    //Todo: just for testing purpose ..
    private var repositoryPhaseCom: PhaseComponentFormJoinRepository = PhaseComponentFormJoinRepository(application)
    private var repoComponentForm: ComponentRepository = ComponentRepository(application)

    private var allPhases: LiveData<List<Phases>> = repository.getAllPhases()

    fun insert(phases: Phases) {
        repository.insertPhase(phases)
    }

    fun update(phases: Phases) {
        repository.updatePhase(phases)
    }

    fun getAllPhases(): LiveData<List<Phases>> {
        return allPhases
    }

    fun getAllPhaseComponent(): LiveData<List<PhasesComponent>> {
        return repository.getAllPhaseComponents()
    }

    fun getComponentByPhaseId(phaseId: Int): LiveData<List<Component>> {

        return repositoryPhaseCom.getComponentListByPhaseId(phaseId)
    }

    fun getFormByPComponentId(componentId: Int): LiveData<List<Forms>> {

        return repositoryPhaseCom.getFormListByComponentId(componentId)
    }

    fun getAllComponentForms(): LiveData<List<ComponentForm>>{
        return repoComponentForm.getAllComponentForm()
    }


}