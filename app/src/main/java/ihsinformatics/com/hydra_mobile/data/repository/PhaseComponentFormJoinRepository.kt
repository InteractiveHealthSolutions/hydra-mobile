package ihsinformatics.com.hydra_mobile.data.repository

import android.content.Context
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.ComponentFormJoinDao
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.PhaseComponentMapDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.*
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.PhaseComponentMap
import org.jetbrains.anko.doAsync

class PhaseComponentMapRepository(context: Context) {


    private var phaseComponentMapDao: PhaseComponentMapDao
    private var componentFormJoinDao: ComponentFormJoinDao
    private var context: Context

    init {

        val database: AppDatabase = AppDatabase.getInstance(
            context.applicationContext
        )!!
        phaseComponentMapDao = database.getPhaseComponentMap()
        componentFormJoinDao = database.getComponentFormJoin()
        this.context = context
    }


    fun insert(phasesComponentMap: PhaseComponentMap) {
        doAsync {
            phaseComponentMapDao.insertPhaseComponent(phasesComponentMap)
        }
    }

    fun insertComponentForm(componentFormJoin: ComponentFormJoin) {
        doAsync {
            componentFormJoinDao.insert(componentFormJoin)
        }
    }


    fun updatePhaseComponentMap(phasesComponentMap: PhaseComponentMap) {
        doAsync {
            phaseComponentMapDao.updatePhaseComponent(phasesComponentMap)
        }
    }


    fun getFormListByComponentId(componentId: Int): List<Forms> {
        return componentFormJoinDao.getComponentFormList(componentId)
    }

    fun getPhaseComponentMapListByPhaseId(phaseId: String): List<PhaseComponentMap> {
        return ArrayList<PhaseComponentMap>()

    }

    fun getComponentListByPhaseUUID(phaseId: String): List<PhaseComponentMap> {
        return phaseComponentMapDao.getPhaseComponentByPhaseUUID(phaseId)

    }


}