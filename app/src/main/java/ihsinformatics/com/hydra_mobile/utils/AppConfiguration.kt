package ihsinformatics.com.hydra_mobile.utils

import android.app.Application
import android.content.Context
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import ihsinformatics.com.hydra_mobile.data.repository.AppSettingRepository
import kotlinx.coroutines.runBlocking

class AppConfiguration() {

    fun getBaseUrl(context: Context): String {
        var baseUrl = ""
        var repository = AppSettingRepository(context)
        runBlocking {
            var list = repository.getSettingList()
            if (list.isNotEmpty()) {
                val setting: AppSetting = list.get(0)
                if (setting.ssl) {
                    baseUrl = "https://" + setting.ip + ":" + setting.port + "/openmrs/ws/rest/v1/"
                } else {
                    //baseUrl = "http://" + setting.ip + ":" + setting.port + "/openmrs/ws/rest/v1/"
                    baseUrl = "http://" + setting.ip+ ":" + setting.port + "/openmrs/ws/rest/v1/"
                }
            } else {
                baseUrl = "http://" + "ihs.ihsinformatics.com"+ ":" + "6811" + "/openmrs/ws/rest/v1/"
              //  baseUrl = "http://" + "199.172.1.215"+ ":" + "3000/"

            }
        }
        return baseUrl
    }
}