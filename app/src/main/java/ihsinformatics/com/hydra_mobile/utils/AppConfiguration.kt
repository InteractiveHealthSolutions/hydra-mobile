package ihsinformatics.com.hydra_mobile.utils

import android.app.Application
import android.content.Context
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import ihsinformatics.com.hydra_mobile.data.repository.AppSettingRepository
import kotlinx.coroutines.runBlocking

class AppConfiguration() {

    fun getBaseUrl(context: Context): String {
        var baseUrl =
            "https://" + "hydraapi.ihsinformatics.com" + ":" + "443" + "/openmrs/ws/rest/v1/"
        var repository = AppSettingRepository(context)
        runBlocking {
            var list = repository.getSettingList()
            if (list.isNotEmpty()) {
                val setting: AppSetting = list.get(0)

                if (null != setting.ip && !setting.ip.equals("") && !setting.ip.equals(" ") && null != setting.port && !setting.port.equals("") && !setting.port.equals(" "))
                    if(setting.ssl)
                    baseUrl = "https://" + setting.ip + ":" + setting.port + "/openmrs/ws/rest/v1/"
                    else
                        baseUrl = "http://" + setting.ip + ":" + setting.port + "/openmrs/ws/rest/v1/"
            }
        }
        return baseUrl
    }
}