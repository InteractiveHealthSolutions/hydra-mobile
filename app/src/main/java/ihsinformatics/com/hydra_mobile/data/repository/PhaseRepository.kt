package ihsinformatics.com.hydra_mobile.data.repository


import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.PhasesDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Phases
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.PhaseApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkFlowApiResponse
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import org.jetbrains.anko.doAsync


class PhaseRepository(context: Context) {
    private var phasesDao: PhasesDao
    private var context: Context
    private val sessionManager = SessionManager(context)

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


    fun getRemotePhaseData(){
        RequestManager(
            context, sessionManager.getUsername(),
            sessionManager.getPassword()
        ).getPhases(
            Constant.REPRESENTATION,
            object :
                RESTCallback {
                override fun <T> onSuccess(o: T) {

                    try {
                        val response = (o as PhaseApiResponse)
                        for (i in response.phase.indices) {
                            //insert into local database
                            insertPhase(response.phase[i])
                        }
                        Log.e("PhaseLoading", "completed")
                    } catch (e: Exception) {

                    }
                }

                override fun onFailure(t: Throwable) {

                }
            })
    }

}