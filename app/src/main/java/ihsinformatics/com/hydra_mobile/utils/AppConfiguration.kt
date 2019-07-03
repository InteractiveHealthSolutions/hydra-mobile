package ihsinformatics.com.hydra_mobile.utils

import android.app.Application
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import ihsinformatics.com.hydra_mobile.repository.AppSettingRepository
import kotlinx.coroutines.runBlocking

class AppConfiguration {

    companion object {
        fun getBaseUrl(application: Application): String {
            var baseUrl = ""
            var repository = AppSettingRepository(application)
            runBlocking {
                var list = repository.getSettingList()
                if (list.isNotEmpty()) {
                    val setting: AppSetting = list.get(0)
                    if (setting.ssl) {
                        baseUrl = "https://" + setting.ip + ":" + setting.port + "/openmrs/ws/rest/v1/"
                    } else {
                        //baseUrl = "http://" + setting.ip + ":" + setting.port + "/openmrs/ws/rest/v1/"
                        baseUrl = "http://" + setting.ip + "/openmrs/ws/rest/v1/"
                    }
                } else {
                    baseUrl = ""
                }
            }
            return baseUrl
        }
    }
}