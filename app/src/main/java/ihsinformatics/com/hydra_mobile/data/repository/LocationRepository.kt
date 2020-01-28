package ihsinformatics.com.hydra_mobile.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.LocationDao
import ihsinformatics.com.hydra_mobile.data.local.dao.commonLab.LabTestTypeDao
import ihsinformatics.com.hydra_mobile.data.local.entities.Location
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestAllType
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.LabTestTypeApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.LocationApiResponse
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import org.jetbrains.anko.doAsync

class LocationRepository(context: Context) {


    private var locationDao: LocationDao
    private val sessionManager = SessionManager(context)
    lateinit var context: Context

    init {

        val database: AppDatabase = AppDatabase.getInstance(context.applicationContext)!!
        locationDao = database.getLocationDao()
        //  phaseComponentJoinDao =   database.getPhaseComponentJoin()
        this.context = context
    }


    fun insertLocation(location: Location) {

            locationDao.insertLocation(location)

    }

    fun getAllLocations(): List<Location> {
        return locationDao.getAllLocation()
    }

    fun deleteAllLocations() {
        doAsync {
            locationDao.deleteLocation()
        }
    }


    fun getRemoteLocationsData() {
        RequestManager(context, sessionManager.getUsername(), sessionManager.getPassword()).getLocation(Constant.REPRESENTATION, object : RESTCallback {
            override fun <T> onSuccess(o: T) {

                try {
                    val response = (o as LocationApiResponse)
                    for (i in response.result.indices) {
                        //insert into local database
                        insertLocation(response.result[i])
                    }

                    Log.e("Location", "completed")
                } catch (e: Exception) {
                    Log.e(e.message, "incompleted")
                    ToastyWidget.getInstance().displayError(context, e.message, Toast.LENGTH_LONG)
                }
            }

            override fun onFailure(t: Throwable) {

            }
        })
    }

}