package ihsinformatics.com.hydra_mobile.data.repository


import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.PhasesDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Phases
import org.jetbrains.anko.doAsync


class PhaseRepository(context: Context) {
    private var phasesDao: PhasesDao
    private var context: Context

    init {
        val database: AppDatabase = AppDatabase.getInstance(
            context.applicationContext
        )!!
        phasesDao = database.getPhaseDao()
        this.context = context
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

}