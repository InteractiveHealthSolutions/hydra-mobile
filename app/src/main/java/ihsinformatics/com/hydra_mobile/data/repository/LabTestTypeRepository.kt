package ihsinformatics.com.hydra_mobile.data.repository

import android.content.Context
import android.util.Log
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.commonLab.LabTestTypeDao
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestAllType
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.LabTestTypeApiResponse
import ihsinformatics.com.hydra_mobile.data.services.manager.RetrofitResponseListener
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


    fun insert(labTest: LabTestAllType) {
        doAsync {
            labTestTypeDao.insert(labTest)
        }
    }

    fun getAllTestTypes():List<LabTestAllType>
    {
        return labTestTypeDao.getAllLabTestTypes()
    }

    fun deleteAllLabTestType() {
        doAsync {
            labTestTypeDao.deleteAllLabTestType()
        }
    }


    fun getRemoteLabTestTypesData(retrofitResponseListener: RetrofitResponseListener) {
        RequestManager(
            context, sessionManager.getUsername(),
            sessionManager.getPassword()
        ).getLabTestTypeResult(
            Constant.REPRESENTATION,
            object :
                RESTCallback {
                override fun <T> onSuccess(o: T) {

                    try {
                        val response = (o as LabTestTypeApiResponse)
                        for (i in response.result.indices) {
                            //insert into local database
                            insert(response.result[i])
                        }
                        retrofitResponseListener.onSuccess()
                        Log.e("LabTestType", "completed")
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