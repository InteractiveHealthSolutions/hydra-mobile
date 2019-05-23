package ihsinformatics.com.hydra_mobile.repository

import android.app.Application
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.AppSettingDao
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import org.jetbrains.anko.doAsync
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


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

    suspend fun getSettingList(): List<AppSetting> {
        var appSettingList = GlobalScope.async {
            appSettingDao.getAllSetting()
        }
        return appSettingList.await()
    }

    fun insertSetting(appSetting: AppSetting) {
        doAsync {
            appSettingDao.getAllSetting()
            appSettingDao.insertSetting(appSetting)
        }
    }

    fun updateSetting(appSetting: AppSetting) {
        doAsync {
            appSettingDao.updateSetting(appSetting)
        }
    }


}