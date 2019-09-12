package ihsinformatics.com.hydra_mobile.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.FormDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Forms
import org.jetbrains.anko.doAsync

class FormRepository(application: Application) {

    private var formDao: FormDao
    private var application: Application

    init {

        val database: AppDatabase = AppDatabase.getInstance(
            application.applicationContext
        )!!
        formDao = database.getForm()
        this.application = application
    }


    fun insertForm(form: Forms) {
        doAsync {
            formDao.insertForm(form)
        }
    }

    fun updateComponent(form: Forms) {
        doAsync {
            formDao.updateForm(form)
        }
    }

    fun getAllForm(): LiveData<List<Forms>> {
        return formDao.getAllForm()
    }

}