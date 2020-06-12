package ihsinformatics.com.hydra_mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Phases
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.WorkFlow
import ihsinformatics.com.hydra_mobile.data.repository.PhaseRepository
import ihsinformatics.com.hydra_mobile.data.repository.WorkFlowRepository


class PhaseViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: PhaseRepository = PhaseRepository(application)


    fun getPhaseUUIDByName(name:String): Phases {
        return repository.getPhaseUUIDByName(name)
    }



}