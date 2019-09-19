package ihsinformatics.com.hydra_mobile.ui.viewmodel


import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.common.NetworkBoundResource
import ihsinformatics.com.hydra_mobile.common.Resource
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.PhaseComponentDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.*
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesMap
import ihsinformatics.com.hydra_mobile.data.repository.ComponentRepository
import ihsinformatics.com.hydra_mobile.data.repository.PhaseComponentFormJoinRepository
import ihsinformatics.com.hydra_mobile.data.repository.PhaseRepository
import io.reactivex.Flowable
import io.reactivex.Observable

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


    public fun loadPhases(): Observable<Resource<List<WorkflowPhasesMap>>> {
        return object : NetworkBoundResource<List<WorkflowPhasesMap>, WorkflowPhasesApiResponse>() {
            override fun createCall(): Observable<Resource<WorkflowPhasesApiResponse>> {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun saveCallResult(@NonNull item: WorkflowPhasesApiResponse) {

            }

            override fun shouldFetch(): Boolean {
                return true
            }

            @NonNull
            override fun loadFromDb(): Flowable<List<WorkflowPhasesMap>> {
                return Flowable.empty<List<WorkflowPhasesMap>>()
            }


        }.asObservable

    }
}