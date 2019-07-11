package ihsinformatics.com.hydra_mobile.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ihsinformatics.com.hydra_mobile.HydraApp
import ihsinformatics.com.hydra_mobile.di.module.ActivityModule
import ihsinformatics.com.hydra_mobile.di.module.ApiModule
import ihsinformatics.com.hydra_mobile.utils.AppConfiguration
import javax.inject.Singleton
import dagger.android.AndroidInjectionModule


/*
 * We mark this interface with the @Component annotation.
 * And we define all the modules that can be injected.
 * Note that we provide AndroidSupportInjectionModule.class
 * here. This class was not created by us.
 * It is an internal class in Dagger 2.10.
 * Provides our activities and fragments with given module.
 *
 * Author: shujaat ali
 * Email: shujaat.ali@ihsinformatics.com
 * */

@Component(
    modules = [
        ApiModule::class,
        ActivityModule::class,
        AndroidInjectionModule::class
    ]
)
@Singleton
interface AppComponent {


    /* We will call this builder interface from our custom Application class.
     * This will set our application object to the AppComponent.
     * So inside the AppComponent the application instance is available.
     * So this application instance can be accessed by our modules
     * such as ApiModule when needed
     * */

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(hydraApp: HydraApp)

}