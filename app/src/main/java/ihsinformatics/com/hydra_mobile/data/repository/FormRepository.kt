
package ihsinformatics.com.hydra_mobile.data.repository

import android.content.Context
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

    fun deleteAllForms() {
        doAsync {
            formDao.deleteAllForms()
        }
    }
}