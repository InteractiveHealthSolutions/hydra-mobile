package ihsinformatics.com.hydra_mobile.repository

import android.app.Application
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.persistentdata.AppDatabase
import ihsinformatics.com.hydra_mobile.persistentdata.dao.AppSettingDao
import ihsinformatics.com.hydra_mobile.persistentdata.entities.AppSetting
import org.jetbrains.anko.doAsync
import io.reactivex.Observable


class AppSettingRepository(application: Application) {

    private var appSettingDao: AppSettingDao
    private var application: Application
    init {
        val database: AppDatabase = AppDatabase.getInstance(
            application.applicationContext
        )!!
        appSettingDao = database.appSettingDao()
        this.application = application
    }

    fun getSettingList(): Observable<List<AppSetting>> {
        return appSettingDao.getAllSetting()
    }

    fun insertSetting(appSetting: AppSetting) {
        doAsync {
            appSettingDao.insertSetting(appSetting)
        }
    }


}