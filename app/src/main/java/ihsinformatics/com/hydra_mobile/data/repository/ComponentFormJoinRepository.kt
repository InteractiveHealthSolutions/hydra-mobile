package ihsinformatics.com.hydra_mobile.data.repository

import android.content.Context
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.ComponentFormJoinDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.*
import org.jetbrains.anko.doAsync

class ComponentFormJoinRepository(context: Context) {


    private var ComponentFormJoinDao: ComponentFormJoinDao
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

}