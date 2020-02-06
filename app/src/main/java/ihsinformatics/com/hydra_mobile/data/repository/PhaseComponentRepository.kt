package ihsinformatics.com.hydra_mobile.data.repository


import android.content.Context
import android.util.Log
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.PhaseComponentMapDao
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.PhaseComponentApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.PhaseComponentMap
import ihsinformatics.com.hydra_mobile.data.services.manager.RetrofitResponseListener
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import org.jetbrains.anko.doAsync


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

//    fun getComponentListByPhaseUUID(phaseId: String): List<PhaseComponentMap> {
//        return phaseComponentMapDao.getPhaseComponentByPhaseUUID(phaseId)
//
//    }

    fun deleteAllPhaseComponents() {
        doAsync {
            phaseComponentMapDao.deleteAllPhaseComponent()
        }
    }


    fun getRemotePhaseComponentMapData(retrofitResponseListener:RetrofitResponseListener) {

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
                        retrofitResponseListener.onSuccess()
                        Log.e("PhaseComponentLoading", "completed")
                    } catch (e: Exception) {
                        retrofitResponseListener.onFailure()
                    }
                }

                override fun onFailure(t: Throwable) {
                    retrofitResponseListener.onFailure()
                }
            })
    }


}