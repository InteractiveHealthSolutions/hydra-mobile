package ihsinformatics.com.hydra_mobile.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.ihsinformatics.dynamicformsgenerator.Utils
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess
import com.ihsinformatics.dynamicformsgenerator.data.utils.JsonHelper
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.LocationDao
import ihsinformatics.com.hydra_mobile.data.local.entities.Location
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.LocationApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.SystemSettingApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.services.manager.RetrofitResponseListener
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import org.jetbrains.anko.doAsync
import org.json.JSONArray

class RelatedDataRepository(context: Context) {


    private var locationDao: LocationDao
    private val sessionManager = SessionManager(context)
    lateinit var context: Context
    private lateinit var dataacess: DataAccess

    init {

        val database: AppDatabase = AppDatabase.getInstance(context.applicationContext)!!
        locationDao = database.getLocationDao()
        //  phaseComponentJoinDao =   database.getPhaseComponentJoin()
        this.context = context
        this.dataacess = DataAccess.getInstance()
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


    fun getRemoteLocationsData(retrofitResponseListener: RetrofitResponseListener) {
        RequestManager(context, sessionManager.getUsername(), sessionManager.getPassword()).getLocation(Constant.REPRESENTATION, object : RESTCallback {
            override fun <T> onSuccess(o: T) {

                try {
                    val response = (o as LocationApiResponse)

                    var location = Utils.convertLocationDTOToLocation(response.result)

                    dataacess.insertLocations(context, location)

                    retrofitResponseListener.onSuccess()
                    Log.e("Location", "completed")
                } catch (e: Exception) {
                    Log.e(e.message, "incompleted")
                    ToastyWidget.getInstance().displayError(context, e.message, Toast.LENGTH_LONG)
                    retrofitResponseListener.onFailure()
                }
            }

            override fun onFailure(t: Throwable) {
                retrofitResponseListener.onFailure()
            }
        })
    }


    fun getRemoteLocationsAndCurrencyData(retrofitResponseListener:RetrofitResponseListener) {
        RequestManager(context, sessionManager.getUsername(), sessionManager.getPassword()).getLocationAndCurrency(Constant.HYDRA_QUERY, Constant.REPRESENTATION, object : RESTCallback {
            override fun <T> onSuccess(o: T) {

                try {
                    val response = (o as SystemSettingApiResponse)

                    dataacess.insertSystemSettings(context, response.result)

                    retrofitResponseListener.onSuccess()

                    Log.e("Location And Currency", "completed")
                } catch (e: Exception) {
                    Log.e(e.message, "incompleted")
                    ToastyWidget.getInstance().displayError(context, e.message, Toast.LENGTH_LONG)
                    retrofitResponseListener.onFailure()
                }
            }

            override fun onFailure(t: Throwable) {
                retrofitResponseListener.onFailure()
            }
        })
    }

}