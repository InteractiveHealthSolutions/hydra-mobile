package ihsinformatics.com.hydra_mobile.utils

import android.app.Application
import android.content.Context
import android.util.Patterns
import android.webkit.URLUtil
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import ihsinformatics.com.hydra_mobile.data.repository.AppSettingRepository
import kotlinx.coroutines.runBlocking

class AppConfiguration() {

    fun getBaseUrl(context: Context): String {

        //Todo Need to change here before release or update of app on playstore by changing it to live server ip address and port  ~Taha
        var openMRSEndpoint = context.getString(R.string.openmrs_endpoint)
        var test_server_ip = context.getString(R.string.live_ip_address)
        var test_server_port = context.getString(R.string.live_port_number)
        var withoutSSL = context.getString(R.string.without_ssl)
        var withSSL = context.getString(R.string.with_ssl)

        //Todo Need to change here before release or update of app on playstore by uncommenting this line ~Taha
        //  var baseUrl = withSSL+test_server_ip+test_server_port+openMRSEndpoint
        var baseUrl = withoutSSL + test_server_ip + ":" + test_server_port + openMRSEndpoint
        var repository = AppSettingRepository(context)
        runBlocking {
            var list = repository.getSettingList()
            if (list.isNotEmpty()) {
                val setting: AppSetting = list.get(0)

                if (null != setting.ip && !setting.ip.equals("") && !setting.ip.equals(" ") && null != setting.port && !setting.port.equals("") && !setting.port.equals(" ")) if (setting.ssl) baseUrl = withSSL + setting.ip + ":" + setting.port + openMRSEndpoint
                else baseUrl = withoutSSL + setting.ip + ":" + setting.port + openMRSEndpoint
            }
        }
        if (URLUtil.isValidUrl(baseUrl) && Patterns.WEB_URL.matcher(baseUrl).matches()) return baseUrl
        else   //TODO need to check what to send when url is invalid  ~Taha
            return baseUrl

    }
}