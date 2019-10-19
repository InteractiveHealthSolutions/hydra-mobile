package ihsinformatics.com.hydra_mobile.data.services.manager

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.ListenableWorker
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.core.question.Question
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.*
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.repository.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

class MetaDataHelper(context: Context) {

    lateinit var workflowPhasesRepository: WorkflowPhasesRepository
    var context: Context


    init {
        this.context = context
        workflowPhasesRepository = WorkflowPhasesRepository(context)
    }

    fun getAllMetaData(restCallback: RESTCallback) = try {
        getWorkFlowFromAPI()
        parseMetaData(object : RESTCallback {

                override fun onFailure(t: Throwable) {
                    restCallback.onFailure(t)
                }

                override fun <T> onSuccess(o: T) {
                    restCallback.onSuccess(o)
                }
            }
        )


    } catch (e: Throwable) {
        Log.e(LOG_TAG, "Error executing work: " + e.message, e)
        false
    }


    fun getWorkFlowFromAPI() {
        workflowPhasesRepository.getRemoteWorkflowData()
    }


    //Todo: remove  in production and sync with api
    private fun parseMetaData(restCallback: RESTCallback) {
        val phaseComponentFormJoinRepository = PhaseComponentFormJoinRepository(context)
        val componentFormJoinRepository = ComponentFormJoinRepository(context)
        try {
            val completeFile = JSONArray(loadJSONFromAsset())

            for (j in 0 until completeFile.length()) {
                val workflow = completeFile.getJSONObject(j)
                val workFlowId = workflow.getInt("id")
                val workflowName = workflow.getString("name")
                val phasesArray = workflow.getJSONArray("phases")

                for (i in 0 until phasesArray.length()) {
                    val insidePhase = phasesArray.getJSONObject(i)
                    val phaseName = insidePhase.getString("name")
                    val phaseId = insidePhase.getInt("id")

                    val components = insidePhase.getJSONArray("components")
                    for (j in 0 until components.length()) {
                        val insideComponent = components.getJSONObject(j)
                        val componentName = insideComponent.getString("name")
                        val componentId = insideComponent.getInt("id")
                        val formList = insideComponent.getJSONArray("forms")
                        for (k in 0 until formList.length()) {
                            val insideForm = formList.getJSONObject(k)
                            val formName = insideForm.getString("encounterType")
                            val formId = insideForm.getInt("formId")
/*                            val questions = insidePhase.getJSONArray("question")


                            for(m in 0 until questions.length())
                            {
                                val insideQuestions = questions.getJSONObject(m)
                                val questionText = insideQuestions.getString("text")

                                val questionDataType = insideQuestions.getString("dataType")
                                val questionOptions = insideQuestions.getString("options")
                                val questionAppearance = insideQuestions.getString("appearance")
                                val questionVariableName = insideQuestions.getString("variableName")
                                val questionConceptId = insideQuestions.getString("conceptId")
                                val questionLength = insideQuestions.getString("length")
                                val questionRequired = insideQuestions.getString("required")
                                val questionDefault = insideQuestions.getString("default")
                                //val questionSkipLogic = insideQuestions.getString("skipLogic")
                                val questionComments = insideQuestions.getString("comments")


                                QuestionRepository(context).insertQuestion(Question())

                            }*/

                            FormRepository(context).insertForm(Forms(formId, formName, componentId, formName))
                            componentFormJoinRepository.insert(ComponentFormJoin(componentId, formId))
                        }


                        ComponentRepository(context).insertComponent(Component(componentName, componentId))
                        phaseComponentFormJoinRepository.insert(PhasesComponentJoin(phaseId, componentId))
                    }

                    PhaseRepository(context).insertPhase(Phases(phaseName, phaseId))
                   // WorkFlowPhasesJoinRepository(context).insert(WorkFlowPhasesJoin(workFlowId, phaseId))

                }
                WorkFlowRepository(context).insertWorkFlow(WorkFlow(workFlowId, workflowName))
                //restCallback.onSuccess(true)
            }
            restCallback.onSuccess(true)

        } catch (e: JSONException) {
            e.printStackTrace()
            restCallback.onFailure(e)
        }
        restCallback.onSuccess(false)
    }


    private fun loadJSONFromAsset(): String? {
        var json: String? = null
        try {
            val `is` = context.assets.open("workflow.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return json
        }
        return json
    }


    companion object {
        private const val LOG_TAG = "WorkflowWorker"
    }
}