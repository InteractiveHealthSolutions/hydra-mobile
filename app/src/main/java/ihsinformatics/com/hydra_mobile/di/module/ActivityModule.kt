package ihsinformatics.com.hydra_mobile.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ihsinformatics.com.hydra_mobile.ui.activity.LoginActivity

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeLoginActivity(): LoginActivity
}