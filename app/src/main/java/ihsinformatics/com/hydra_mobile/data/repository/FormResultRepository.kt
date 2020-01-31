package ihsinformatics.com.hydra_mobile.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.ihsinformatics.dynamicformsgenerator.common.Constants
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.FormDao
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.FormsResultDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.ComponentFormJoin
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Forms
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.WorkFlow
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.FormApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.FormResultApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.MyFormResultApiResponse
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import org.jetbrains.anko.doAsync
import org.json.JSONException
import org.json.JSONObject

class FormResultRepository(context: Context) {

    private var formResultDao: FormsResultDao
    lateinit var componentFormJoinRepository: ComponentFormJoinRepository
    private var context: Context
    private val sessionManager = SessionManager(context)

    init {

        val database: AppDatabase = AppDatabase.getInstance(
            context.applicationContext
        )!!
        formResultDao = database.getFormsResult()
        this.context = context

        componentFormJoinRepository = ComponentFormJoinRepository(context)
    }


    fun insertFormResults(formResult: FormResultApiResponse) {
        doAsync {
            formResultDao.insertFormResult(formResult)
        }
    }

    fun getFormResults(): FormResultApiResponse {
        return formResultDao.getFormResults()

    }

    fun deleteFormResult() {
        formResultDao.deleteFormResult()

    }


    fun getRemoteFormResultData() {
        RequestManager(
            context, sessionManager.getUsername(),
            sessionManager.getPassword()
        ).getFormsResult(
            object :
                RESTCallback {
                override fun <T> onSuccess(o: T) {

                    try {
                        val response = (o as String)
                      //  insertFormResults(response)

                        parseForms(response)

                        Log.e("FormLoading", "completed")
                    } catch (e: Exception) {

                    }
                }

                override fun onFailure(t: Throwable) {

                }
            })
    }

    private fun parseForms(result:String)
    {
        try {

            val completeFile= JSONObject(result)
            val formList=completeFile.getJSONArray("forms")

            for (k in 0 until formList.length()) {

                val insideForm = formList.getJSONObject(k)
                val formName = insideForm.getString("name")
                val formId = insideForm.getInt("hydramoduleFormId")
                val questionsList = insideForm.getJSONArray("formFields")

                //  componentFormJoinRepository.insert(ComponentFormJoin(componentId, formId))
                FormRepository(context).insertForm(
                    Forms(
                        formId,
                        formName,
                        3,
                        formName,
                        questionsList.toString()
                    )
                )


                Constants.getInstance().encounterTypes.put(formId, formName)
                Constants.getInstance().encounterTypesData.put(
                    formName,
                    questionsList.toString()
                )

                componentFormJoinRepository.insert(ComponentFormJoin(k, 3, formId))
            }


        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}