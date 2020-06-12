package ihsinformatics.com.hydra_mobile.data.repository

import android.content.Context
import android.util.Log
import com.ihsinformatics.dynamicformsgenerator.common.Constants
import com.ihsinformatics.dynamicformsgenerator.common.FormDetails
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.FormDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Forms
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.FormApiResponse
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import org.jetbrains.anko.doAsync

class FormRepository(context: Context) {

    private var formDao: FormDao
    private var context: Context
    private val sessionManager = SessionManager(context)

    init {

        val database: AppDatabase = AppDatabase.getInstance(context.applicationContext)!!
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

    fun getAllForms(): List<Forms> {
        return formDao.getAllForms()

    }

    fun getAllFormsByFilter(component:String,phase:String,workflow:String): List<Forms> {
        return formDao.getAllFormsByFilter(component,phase,workflow)

    }


}