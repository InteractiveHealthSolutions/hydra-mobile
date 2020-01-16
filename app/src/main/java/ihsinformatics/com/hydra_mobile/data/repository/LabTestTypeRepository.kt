package ihsinformatics.com.hydra_mobile.data.repository

import android.content.Context
import android.util.Log
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.commonLab.LabTestTypeDao
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.ComponentFormJoinDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.*
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestType
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestTypeApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.ComponentApiResponse
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import org.jetbrains.anko.doAsync

class LabTestTypeRepository(context: Context) {


    private var labTestTypeDao: LabTestTypeDao
    private var context: Context
    private val sessionManager = SessionManager(context)

    init {

        val database: AppDatabase = AppDatabase.getInstance(
            context.applicationContext
        )!!
        labTestTypeDao = database.getLabTestTypeDao()
        //  phaseComponentJoinDao =   database.getPhaseComponentJoin()
        this.context = context
    }


    fun insert(labTest: LabTestType) {
        doAsync {
            labTestTypeDao.insert(labTest)
        }
    }

    fun getAllTestTypes():List<LabTestType>
    {
        return labTestTypeDao.getAllLabTestTypes()
    }

    fun deleteAllLabTestType() {
        doAsync {
            labTestTypeDao.deleteAllLabTestType()
        }
    }


    fun getRemoteLabTestTypesData() {
        RequestManager(
            context, sessionManager.getUsername(),
            sessionManager.getPassword()
        ).getLabTestTypeResult(
            object :
                RESTCallback {
                override fun <T> onSuccess(o: T) {

                    try {
                        val response = (o as LabTestTypeApiResponse)
                        for (i in response.result.indices) {
                            //insert into local database
                            insert(response.result[i])
                        }

                        Log.e("LabTestType", "completed")
                    } catch (e: Exception) {

                    }
                }

                override fun onFailure(t: Throwable) {

                }
            })
    }

}