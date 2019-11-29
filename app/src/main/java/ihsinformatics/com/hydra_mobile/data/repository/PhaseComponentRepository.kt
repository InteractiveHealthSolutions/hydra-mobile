package ihsinformatics.com.hydra_mobile.data.repository


import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.ComponentFormJoinDao
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.PhaseComponentJoinDao
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.PhaseComponentMapDao
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.WorkflowPhasesMapDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Component
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.ComponentFormJoin
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Forms
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.PhasesComponentJoin
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.PhaseComponentApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.PhaseComponentMap
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesMap
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.jetbrains.anko.doAsync
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets


class PhaseComponentRepository(context: Context) {

  //  private var phaseComponentJoinDao: PhaseComponentJoinDao
    private var phaseComponentMapDao: PhaseComponentMapDao
    private var context: Context
    private val sessionManager = SessionManager(context)

    init {

        val database: AppDatabase = AppDatabase.getInstance(
            context.applicationContext
        )!!
        phaseComponentMapDao = database.getPhaseComponentMap()
        this.context = context
    }


    fun insert(phasesComponentMap: PhaseComponentMap) {
        doAsync {
            phaseComponentMapDao.insertPhaseComponent(phasesComponentMap)
        }
    }


    fun updatePhaseComponentJoin(phaseComponentMap: PhaseComponentMap) {
        doAsync {
            phaseComponentMapDao.updatePhaseComponent(phaseComponentMap)
        }
    }



    fun getPhaseComponentMapListByPhaseId(phaseId: Int): List<PhaseComponentMap> {
        return ArrayList<PhaseComponentMap>()

    }

    fun getComponentListByPhaseUUID(phaseId: String): List<PhaseComponentMap> {
        return phaseComponentMapDao.getPhaseComponentByPhaseUUID(phaseId)

    }

    fun deleteAllPhaseComponents() {
        doAsync {
            phaseComponentMapDao.deleteAllPhaseComponent()
        }
    }


    fun getRemotePhaseComponentMapData() {

        RequestManager(
            context, sessionManager.getUsername(),
            sessionManager.getPassword()
        ).getPhaseComponentMap(
            Constant.REPRESENTATION,
            object :
                RESTCallback {
                override fun <T> onSuccess(o: T) {

                    try {
                        val response = (o as PhaseComponentApiResponse)
                        for (i in response.phaseComponentMap.indices) {
                            //insert into local database
                            insert(response.phaseComponentMap[i])
                        }
                        Log.e("PhaseComponentLoading", "completed")
                    } catch (e: Exception) {

                    }
                }

                override fun onFailure(t: Throwable) {

                }
            })
    }


}