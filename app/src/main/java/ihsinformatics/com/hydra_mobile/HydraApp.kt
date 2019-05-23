package ihsinformatics.com.hydra_mobile

import android.app.Activity
import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import ihsinformatics.com.hydra_mobile.repository.AppSettingRepository
import ihsinformatics.com.hydra_mobile.viewmodel.AppSettingViewModel

/**
 * File Description
 * <p>
 * Author: shujaat ali
 * Email: shujaat.ali@ihsinformatics.com
 */


class HydraApp : Application() {

    companion object {
        val instance = HydraApp()
    }

    override fun onCreate() {
        super.onCreate()
        initializeAppSetting()
    }


    private fun initializeAppSetting() {
        val appSettingRepo = AppSettingRepository(this)
        appSettingRepo.insertSetting(
            AppSetting(
                getString(R.string.default_ip_address),
                getString(R.string.default_port_number),
                false
            )
        )
       // Toast.makeText(this, "saved default settings", Toast.LENGTH_SHORT).show()
    }


}