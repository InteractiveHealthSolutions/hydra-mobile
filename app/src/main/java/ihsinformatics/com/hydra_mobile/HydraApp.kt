package ihsinformatics.com.hydra_mobile

import android.app.Activity
import android.app.Application
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import ihsinformatics.com.hydra_mobile.di.component.DaggerAppComponent
import ihsinformatics.com.hydra_mobile.data.repository.AppSettingRepository
import javax.inject.Inject

/**
 * File Description
 * <p>
 * Author: shujaat ali
 * Email: shujaat.ali@ihsinformatics.com
 */


class HydraApp : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }


    override fun onCreate() {
        super.onCreate()
        initializeAppSetting()
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
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