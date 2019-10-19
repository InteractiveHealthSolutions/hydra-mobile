package ihsinformatics.com.hydra_mobile.data.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.FormDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Forms
import org.jetbrains.anko.doAsync

class FormRepository(context: Context) {

    private var formDao: FormDao
    private var context: Context

    init {

        val database: AppDatabase = AppDatabase.getInstance(
            context.applicationContext
        )!!
        formDao = database.getForm()
        this.context = context
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