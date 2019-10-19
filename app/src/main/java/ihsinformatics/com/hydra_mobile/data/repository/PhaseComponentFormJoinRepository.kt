package ihsinformatics.com.hydra_mobile.data.repository

import android.content.Context
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.ComponentFormJoinDao
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.PhaseComponentJoinDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.jetbrains.anko.doAsync

class PhaseComponentFormJoinRepository(context: Context) {


    private var phaseComponentJoinDao: PhaseComponentJoinDao
    private var componentFormJoinDao: ComponentFormJoinDao
    private var context: Context

    init {

        val database: AppDatabase = AppDatabase.getInstance(
            context.applicationContext
        )!!
        phaseComponentJoinDao = database.getPhaseComponentJoin()
        componentFormJoinDao = database.getComponentFormJoin()
        this.context = context
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


    fun updatePhaseComponentJoin(phasesComponentJoin: PhasesComponentJoin) {
        doAsync {
            phaseComponentJoinDao.updatePhasesComponentJoin(phasesComponentJoin)
        }
    }


    fun getFormListByComponentId(componentId: Int): List<Forms> {
        return componentFormJoinDao.getComponentFormList(componentId)
    }

    fun getPhaseComponentJoinListByPhaseId(phaseId: Int): List<PhasesComponentJoin> {
        return ArrayList<PhasesComponentJoin>()

    }

    fun getComponentListByPhaseId(phaseId: Int): List<Component> {
        return phaseComponentJoinDao.getComponentsByPhaseID(phaseId)

    }


}