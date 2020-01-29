package ihsinformatics.com.hydra_mobile.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess
import com.ihsinformatics.dynamicformsgenerator.data.utils.JsonHelper
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.LocationDao
import ihsinformatics.com.hydra_mobile.data.local.entities.Location
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.LocationApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import org.json.JSONObject

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
                    val dataacess= DataAccess.getInstance()
                    //for (i in response.result) {
                        //insert into local database
                        //insertLocation(response.result[i])
                        dataacess.insertLocations(context, response.result)

                   // }

//                    val result=JSONArray(response.result)
//
//                    if (result != null) {
//                        val locations: List<com.ihsinformatics.dynamicformsgenerator.data.pojos.Location> = JsonHelper.parseLocationsFromJson(result)
//                        DataAccess.getInstance().insertLocations(context, locations)
//                    }
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