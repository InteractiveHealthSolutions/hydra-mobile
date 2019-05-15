package ihsinformatics.com.hydra_mobile.repository

import android.app.Application
import ihsinformatics.com.hydra_mobile.network.manager.RequestManager
import ihsinformatics.com.hydra_mobile.network.model.RESTCallback
import ihsinformatics.com.hydra_mobile.persistentdata.AppDatabase
import ihsinformatics.com.hydra_mobile.persistentdata.dao.AppSettingDao
import ihsinformatics.com.hydra_mobile.utils.Constant

class UserRepository(application: Application) {

    private var application: Application

    init {
        val database: AppDatabase = AppDatabase.getInstance(
            application.applicationContext
        )!!
        this.application = application
    }


    fun userAuthentication(username: String, password: String): Boolean {
        var isAuthenticate = false
        RequestManager.getClient(application, username, password)
        RequestManager.authenticateUser(username, Constant.REPRESENTATION, object : RESTCallback {
            override fun onFailure(t: Throwable) {
                isAuthenticate = false
            }

            override fun onSuccess(o: Any) {
                isAuthenticate = true
                //her we save the  user data into local database


            }
        })
        return isAuthenticate
    }


}