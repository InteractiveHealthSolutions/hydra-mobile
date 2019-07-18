package ihsinformatics.com.hydra_mobile.data.repository

import android.app.Application
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase


class PatientRepository(application: Application) {

    private var application: Application

    init {
        AppDatabase.getInstance(
            application.applicationContext
        )!!
        this.application = application
    }

}