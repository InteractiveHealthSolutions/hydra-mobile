package ihsinformatics.com.hydra_mobile.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.ComponentFormJoinDao
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.PhaseComponentJoinDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Component
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.ComponentFormJoin
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Forms
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.PhasesComponentJoin
import org.jetbrains.anko.doAsync

class PhaseComponentFormJoinRepository(application: Application) {


    private var phaseComponentJoinDao: PhaseComponentJoinDao
    private var componentFormJoinDao: ComponentFormJoinDao
    private var application: Application

    init {

        val database: AppDatabase = AppDatabase.getInstance(
            application.applicationContext
        )!!
        phaseComponentJoinDao = database.getPhaseComponentJoin()
        componentFormJoinDao =   database.getComponentFormJoin()
        this.application = application
    }


    fun insert(phasesComponentJoin: PhasesComponentJoin) {
        doAsync {
            phaseComponentJoinDao.insert(phasesComponentJoin)
        }
    }

    fun insertComponentForm(componentFormJoin: ComponentFormJoin) {
        doAsync {
            componentFormJoinDao.insert(componentFormJoin)
        }
    }

    fun getFormListByComponentId(componentId: Int): LiveData<List<Forms>> {
        return componentFormJoinDao.getComponentFormList(componentId)
    }

    fun getComponentListByPhaseId(phaseId: Int): LiveData<List<Component>> {
        return phaseComponentJoinDao.getPhaseComponentList(phaseId)
    }


}