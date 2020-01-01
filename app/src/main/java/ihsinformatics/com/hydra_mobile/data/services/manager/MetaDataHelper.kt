package ihsinformatics.com.hydra_mobile.data.services.manager

import android.content.Context
import android.text.InputType
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.ihsinformatics.dynamicformsgenerator.common.Constants
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.SExpression
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.SkipLogics
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.QuestionConfiguration
import com.ihsinformatics.dynamicformsgenerator.data.utils.GlobalConstants
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidget
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.common.Constant.Companion.formID
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.ComponentFormJoin
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Forms
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.repository.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class MetaDataHelper(context: Context) {

    lateinit var workflowRepository: WorkFlowRepository
    lateinit var phaseRepository: PhaseRepository
    lateinit var componentRepository: ComponentRepository
    lateinit var formRepository: FormRepository


    lateinit var workflowPhasesRepository: WorkflowPhasesRepository
    lateinit var phaseComponentRepository: PhaseComponentRepository

    var context: Context


    init {
        this.context = context

        workflowRepository = WorkFlowRepository(context)
        phaseRepository = PhaseRepository(context)
        componentRepository = ComponentRepository(context)
        formRepository = FormRepository(context)

        workflowPhasesRepository = WorkflowPhasesRepository(context)
        phaseComponentRepository= PhaseComponentRepository(context)

    }

    fun getAllMetaData(restCallback: RESTCallback) = try {

        deleteExisitingLocalData()

        getWorkFlowFromAPI()
        getPhasesFromAPI()
        getComponentsFromAPI()
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
        workflowRepository.getRemoteWorkFlowData()
    }

    fun getPhasesFromAPI() {
        phaseRepository.getRemotePhaseData()
        phaseComponentRepository.getRemotePhaseComponentMapData()
    }

    fun getComponentsFromAPI() {
        componentRepository.getRemoteComponentData()
    }

    fun deleteExisitingLocalData()
    {
        workflowRepository.deleteAllWorkflow()
        phaseRepository.deleteAllPhases()
        componentRepository.deleteAllComponents()
        formRepository.deleteAllForms()


        workflowPhasesRepository.deleteAllWorkflowPhases()
        phaseComponentRepository.deleteAllPhaseComponents()

    }

    //Todo: remove  in production and sync with api
    private fun parseMetaData(restCallback: RESTCallback) {
       // val phaseComponentFormJoinRepository = PhaseComponentFormJoinRepository(context)
        val componentFormJoinRepository = ComponentFormJoinRepository(context)
        try {
            val completeFile = JSONArray(loadJSONFromAsset())

            for (j in 0 until completeFile.length()) {
                val workflow = completeFile.getJSONObject(j)
                val workFlowId = workflow.getInt("id")
                val workflowName = workflow.getString("name")
                val phasesArray = workflow.getJSONArray("phases")

                //WorkFlowRepository(context).insertWorkFlow(WorkFlow(workFlowId, workflowName))

                for (i in 0 until phasesArray.length()) {
                    val insidePhase = phasesArray.getJSONObject(i)
                    val phaseName = insidePhase.getString("name")
                    val phaseId = insidePhase.getInt("id")

                    val components = insidePhase.getJSONArray("components")

                    //   PhaseRepository(context).insertPhase(Phases(phaseName, phaseId))

                    for (j in 0 until components.length()) {
                        val insideComponent = components.getJSONObject(j)
                        val componentName = insideComponent.getString("name")
                        val componentId = insideComponent.getInt("id")
                        val formList = insideComponent.getJSONArray("forms")

                        //  ComponentRepository(context).insertComponent(Component(componentName, componentId))
                      //  phaseComponentFormJoinRepository.insert(PhasesComponentJoin(phaseId, componentId))

                         for (k in 0 until formList.length()) {

                            val insideForm = formList.getJSONObject(k)
                            val formName = insideForm.getString("encounterType")
                            val formId = insideForm.getInt("formId")
                            val questionsList = insideForm.getJSONArray("questions")

//                             var listOfQuestions=ArrayList<Question>()
//                            for(l in 0 until questionsList.length())
//                            {
//                                val question=questionsList.getJSONObject(l)
//
//                                val config = question.getJSONObject("config")
//                                val id=config.getInt("id")
//                                val inputType=config.getString("inputType")
//                                val keyboardCharacters=config.getString("keyboardCharacters")
//                                val widgetType=config.getString("widgetType")
//                                val minLength=config.getInt("minLength")
//                                val maxLength=config.getInt("maxLength")
//                                val minValue=config.getInt("minValue")
//                                val maxValue=config.getInt("maxValue")
//                                val minDate=config.getString("minDate")
//                                val maxDate=config.getString("maxDate")
//                                val maxLines=config.getInt("maxLines")
//
//                                val configuration: QuestionConfiguration = QuestionConfiguration(
//                                    InputType.TYPE_CLASS_NUMBER,maxLength,minLength,keyboardCharacters,id,maxValue,minValue,maxDate,minDate,maxLines)
//
//                                val questionId=question.getInt("id")
//                                val questionNumber=question.getString("questionNumber")
//                                val description=question.getString("description")
//                                val conceptName=question.getString("conceptName")
//                                val concetUUID=question.getString("concetUUID")
//                                var initialVisibility=question.getString("initialVisibility")
//
//
//                                var required=false
//                                if (question.getInt("required")==0)
//                                {
//                                    required=false
//                                }
//                                else {
//                                    required = true
//                                }
//                                //ToDo discuss about widgetType in wrong field
//                                // QuestionNumber, initialVisiblility and required types, concept Name vs conceptParams and validation function
//                                // Change edittext to proper widget type  ~Taha
//
//
//                                val optionsList=question.getJSONArray("options")
//                                for(m in 0 until optionsList.length())
//                                {
//                                    val option=optionsList.getJSONObject(m)
//                                    val default=option.getInt("default")
//                                    val optionConceptUUID=option.getString("conceptUUID")
//                                    val display=option.getString("display")
//
//                                }
//                                var visible:List<SkipLogics>
//
//
//                                val visibleWhenList=question.getJSONArray("visibleWhen")
//                                val visibleWhen=skipLogicParser(visibleWhenList)
//
//                                val hiddenWhenList=question.getJSONArray("hiddenWhen")
//                                val hiddenWhen=skipLogicParser(hiddenWhenList)
//
//                                val requiredWhenList=question.getJSONArray("requiredWhen")
//                                val requiredWhen=skipLogicParser(requiredWhenList)
//
//                                var completeQuestion:Question = Question(required,formId,questionId,questionNumber,widgetType,initialVisibility,null,description,conceptName,configuration,visibleWhen,hiddenWhen,requiredWhen)
//                                listOfQuestions.add(completeQuestion)
//
//                            }
                            componentFormJoinRepository.insert(ComponentFormJoin(componentId, formId))
                            FormRepository(context).insertForm(Forms(formId, formName, componentId, formName,questionsList.toString()))
                            Constants.getInstance().encounterTypes.put(formId,formName)

                        }


                    }

                    // WorkFlowPhasesJoinRepository(context).insert(WorkFlowPhasesJoin(workFlowId, phaseId))

                }
                //restCallback.onSuccess(true)
            }
            restCallback.onSuccess(true)

        } catch (e: JSONException) {
            e.printStackTrace()
            restCallback.onFailure(e)
        }
        restCallback.onSuccess(false)
    }



    private fun skipLogicParser(sExpressionList:JSONArray):List<SExpression>
    {
        var SExpressionCompleteList=ArrayList<SExpression>()
        var sExpression:SExpression= SExpression()
        for(n in 0 until sExpressionList.length())
        {
            val obj=sExpressionList.get(n)



            if(obj is String)
            {
                sExpression.operator=obj
            }
            else if(obj is JSONObject)
            {

                var s:SkipLogics= SkipLogics()
                //TODO QuestionId must be a string ~Taha
                val skiplogicID=obj.getString("id")
                val skiplogicQuestionId=obj.getInt("questionId")

                s.id=skiplogicID
                s.questionID=skiplogicQuestionId

                val skiplogicEqualList=obj.getJSONArray("equals")
                for(o in 0 until skiplogicEqualList.length()) {
                    val optionUUIDObject = skiplogicEqualList.getJSONObject(o)
                    val optionUUID = optionUUIDObject.getString("uuid")

                    s.equalsList.add(o,optionUUID)
                }

                val skiplogicNotEqualList=obj.getJSONArray("notEquals")
                for(o in 0 until skiplogicNotEqualList.length()) {
                    val optionUUIDObject = skiplogicNotEqualList.getJSONObject(o)
                    val optionUUID = optionUUIDObject.getString("uuid")

                    s.notEqualsList.add(o,optionUUID)
                }

                val skiplogicEqualsTo=obj.getJSONArray("equalTo")
                for(o in 0 until skiplogicEqualsTo.length()) {
                    val optionWithNumbers = skiplogicEqualsTo.getInt(o)

                    s.equalsToList.add(o,optionWithNumbers)
                }

                val skiplogicNotEqualsTo=obj.getJSONArray("notEqualTo")
                for(o in 0 until skiplogicNotEqualsTo.length()) {
                    val optionWithNumbers = skiplogicNotEqualsTo.getInt(o)

                    s.notEqualsToList.add(o,optionWithNumbers)
                }

                val skiplogicLessThan=obj.getJSONArray("lessThan")
                for(o in 0 until skiplogicLessThan.length()) {
                    val optionWithNumbers = skiplogicLessThan.getInt(o)

                    s.lessThanList.add(o,optionWithNumbers)
                }

                val skiplogicGreaterThan=obj.getJSONArray("greaterThan")
                for(o in 0 until skiplogicGreaterThan.length()) {
                    val optionWithNumbers = skiplogicGreaterThan.getInt(o)

                    s.greaterThanList.add(o,optionWithNumbers)
                }

                sExpression.skipLogicsObjects.add(s)
            }
            else if(obj is JSONArray)
            {
                sExpression.skipLogicsArray=skipLogicParser(obj)
            }


        }
        SExpressionCompleteList.add(sExpression)
        return  SExpressionCompleteList
    }

    private fun loadJSONFromFileName(file: String): String? {
        var json: String? = null
        try {
            val `is` = context.assets.open(file)
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