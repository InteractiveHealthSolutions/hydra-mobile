package ihsinformatics.com.hydra_mobile.data.repository

import android.content.Context
import android.util.Log
import com.ihsinformatics.dynamicformsgenerator.common.Constants
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

    fun getRemoteFormData() {
        RequestManager(context, sessionManager.getUsername(), sessionManager.getPassword()).getForms(Constant.REPRESENTATION, object : RESTCallback {
            override fun <T> onSuccess(o: T) {

                try {
                    val response = (o as FormApiResponse)
                    for (i in response.forms.indices) {
                        //insert into local database
                        insertForm(response.forms[i])
                        Constants.setEncounterType(response.forms[i].id, response.forms[i].name)
                        Constants.setEncounterTypeData(response.forms[i].name, response.forms[i].questions)
                    }
                    Log.e("FormLoading", "completed")
                } catch (e: Exception) {

                }
            }

            override fun onFailure(t: Throwable) {

            }
        })
    }
}