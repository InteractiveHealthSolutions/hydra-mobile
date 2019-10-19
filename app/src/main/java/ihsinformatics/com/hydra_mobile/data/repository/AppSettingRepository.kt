package ihsinformatics.com.hydra_mobile.data.repository

import android.app.Application
import android.content.Context
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.AppSettingDao
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import org.jetbrains.anko.doAsync
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


class AppSettingRepository(context: Context) {

    private var appSettingDao: AppSettingDao
    private var context: Context

    init {

        val database: AppDatabase = AppDatabase.getInstance(
            context.applicationContext
        )!!
        appSettingDao = database.appSettingDao()
        this.context = context
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