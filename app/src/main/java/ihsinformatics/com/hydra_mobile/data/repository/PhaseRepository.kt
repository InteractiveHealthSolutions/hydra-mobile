package ihsinformatics.com.hydra_mobile.data.repository


import android.app.Application
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.PhasesDao
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.PhaseComponentDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Phases
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.PhasesComponent
import org.jetbrains.anko.doAsync
import javax.inject.Inject


class PhaseRepository(application: Application) {
    private var phasesDao: PhasesDao
    private var phaseComponentDao: PhaseComponentDao
    private var application: Application

    init {
        val database: AppDatabase = AppDatabase.getInstance(
            application.applicationContext
        )!!
        phasesDao = database.getPhaseDao()
        phaseComponentDao = database.getPhaseComponent()
        this.application = application
    }


    fun insertPhase(phases: Phases) {
        doAsync {
            phasesDao.insertPhase(phases)
        }
    }

    fun updatePhase(phases: Phases) {
        doAsync {
            phasesDao.updatePhase(phases)
        }
    }

    fun getAllPhases(): LiveData<List<Phases>> {
        return phasesDao.getAllPhases()
    }

    fun getAllPhaseComponents(): LiveData<List<PhasesComponent>> {
        return phaseComponentDao.getPhaseComponentList()
    }


}