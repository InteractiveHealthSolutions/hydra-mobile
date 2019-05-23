package ihsinformatics.com.hydra_mobile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import ihsinformatics.com.hydra_mobile.repository.AppSettingRepository

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: AppSettingRepository = AppSettingRepository(application)

    fun insert(setting: AppSetting) {
        repository.insertSetting(setting)
    }

}