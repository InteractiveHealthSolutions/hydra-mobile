package ihsinformatics.com.hydra_mobile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.persistentdata.entities.AppSetting
import ihsinformatics.com.hydra_mobile.repository.AppSettingRepository

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: AppSettingRepository = AppSettingRepository(application)

    fun insert(setting: AppSetting) {
        repository.insertSetting(setting)
    }

    fun update(setting: AppSetting) {

    }

    fun delete(setting: AppSetting) {

    }


}