package ihsinformatics.com.hydra_mobile.data.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.ComponentFormJoinDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.*
import org.jetbrains.anko.doAsync

class ComponentFormJoinRepository(context: Context) {


    private var ComponentFormJoinDao: ComponentFormJoinDao
    //private var phaseComponentJoinDao: PhaseComponentJoinDao
    private var context: Context

    init {

        val database: AppDatabase = AppDatabase.getInstance(
            context.applicationContext
        )!!
        ComponentFormJoinDao = database.getComponentFormJoin()
        //  phaseComponentJoinDao =   database.getPhaseComponentJoin()
        this.context = context
    }


    fun insert(componentFormsJoin: ComponentFormJoin) {
        doAsync {
            ComponentFormJoinDao.insert(componentFormsJoin)
        }
    }

//    fun insertComponentForm(phasesFomponentJoin: PhasesComponentJoin) {
//        doAsync {
//            phaseComponentJoinDao.insert(phasesFomponentJoin)
//        }
//    }

//    fun getFormListByComponentId(componentID: Int): LiveData<List<Forms>> {
//        return ComponentFormJoinDao.getPhaseComponentJoinList(componentID)
//    }

    fun getFormListByComponentId(componentId: Int): List<Forms> {
        return ComponentFormJoinDao.getComponentFormList(componentId)
    }


}