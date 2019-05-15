package ihsinformatics.com.hydra_mobile.utils

import android.app.Application
import ihsinformatics.com.hydra_mobile.persistentdata.entities.AppSetting
import ihsinformatics.com.hydra_mobile.repository.AppSettingRepository

class AppConfiguration {

    companion object {
        fun getBaseUrl(application: Application): String {
            var baseUrl = ""
            var repository = AppSettingRepository(application)

            val settings = repository.getSettingList().subscribe(
                {
                    if (it.isNotEmpty()) {
                        val setting: AppSetting = it.get(0)
                        if (setting.ssl) {
                            baseUrl = "https://" + setting.ip + ":" + setting.port + "/openmrs/ws/rest/v1/"
                        } else {
                            baseUrl = "http://" + setting.ip + ":" + setting.port + "/openmrs/ws/rest/v1/"
                        }
                    }
                }, { e ->
                    baseUrl =""
                }
            )
            return settings.()
        }
    }
}