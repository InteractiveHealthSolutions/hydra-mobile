package ihsinformatics.com.hydra_mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import ihsinformatics.com.hydra_mobile.data.repository.AppSettingRepository

class AppSettingViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: AppSettingRepository = AppSettingRepository(application)

    fun insert(setting: AppSetting) {
        repository.insertSetting(setting)
    }

    fun update(setting: AppSetting) {
        repository.updateSetting(setting)
    }

    fun deleteAll()
    {
        repository.deleteAll()
    }

    suspend fun get(){
        repository.getSettingList()
    }

    fun getSettings(): List<AppSetting>{
        return repository.getSettings()
    }

}