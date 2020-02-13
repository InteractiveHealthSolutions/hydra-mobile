package ihsinformatics.com.hydra_mobile.data.services.manager

import android.content.Context
import android.util.Log
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.SExpression
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.SkipLogics
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

    lateinit var formResultRepository: FormResultRepository

    lateinit var workflowPhasesRepository: WorkflowPhasesRepository
    lateinit var phaseComponentRepository: PhaseComponentRepository

    lateinit var componentFormJoinRepository: ComponentFormJoinRepository

    lateinit var labTestTypeRepository: LabTestTypeRepository

    lateinit var relatedRepository:RelatedDataRepository

    var successCount=0
    var totalCount=9


    var context: Context


    init {
        this.context = context

        workflowRepository = WorkFlowRepository(context)
        phaseRepository = PhaseRepository(context)
        componentRepository = ComponentRepository(context)
        formRepository = FormRepository(context)


        workflowPhasesRepository = WorkflowPhasesRepository(context)
        phaseComponentRepository = PhaseComponentRepository(context)
        componentFormJoinRepository = ComponentFormJoinRepository(context)

        formResultRepository = FormResultRepository(context)

        relatedRepository=RelatedDataRepository(context)
        labTestTypeRepository = LabTestTypeRepository(context)
    }

    fun getAllMetaData(restCallback: RESTCallback) = try {

        parseMetaData(object:RetrofitResponseListener{
            override fun onSuccess() {

                successCount++
                if(successCount==totalCount)
                {
                    restCallback.onSuccess(true)
                }

            }

            override fun onFailure() {
                restCallback.onFailure(Throwable("Some error occured"))
                restCallback.onSuccess(false)
            }
        },object : RESTCallback {

            override fun onFailure(t: Throwable) {

            }

            override fun <T> onSuccess(o: T) {

            }
        })


    } catch (e: Throwable) {
        Log.e(LOG_TAG, "Error executing work: " + e.message, e)
        false
    }




    fun getWorkFlowFromAPI(retrofitResponseListener:RetrofitResponseListener) {
        workflowPhasesRepository.getRemoteWorkflowData(retrofitResponseListener)
        workflowRepository.getRemoteWorkFlowData(retrofitResponseListener)
    }

    fun getPhasesFromAPI(retrofitResponseListener:RetrofitResponseListener) {
        phaseRepository.getRemotePhaseData(retrofitResponseListener)
        phaseComponentRepository.getRemotePhaseComponentMapData(retrofitResponseListener)
    }

    fun getComponentsFromAPI(retrofitResponseListener:RetrofitResponseListener) {
        componentRepository.getRemoteComponentData(retrofitResponseListener)




        // componentFormJoinRepository.getRemoteComponentFormMapData()
    }

    fun getFormsFromAPI(retrofitResponseListener:RetrofitResponseListener) {

        formResultRepository.getRemoteFormResultData(retrofitResponseListener)
    }


    fun getAllLabTestTypesFromAPI(retrofitResponseListener:RetrofitResponseListener)
    {
        labTestTypeRepository.getRemoteLabTestTypesData(retrofitResponseListener)
    }

    fun getAllRelatedData(retrofitResponseListener:RetrofitResponseListener)  //e.g Location, currency, AllLabTestTypes
    {

        getAllLabTestTypesFromAPI(retrofitResponseListener)
        relatedRepository.getRemoteLocationsData(retrofitResponseListener)
        relatedRepository.getRemoteLocationsAndCurrencyData(retrofitResponseListener)

    }


    fun deleteExisitingLocalData() {
        workflowRepository.deleteAllWorkflow()
        phaseRepository.deleteAllPhases()
        componentRepository.deleteAllComponents()
        formRepository.deleteAllForms()


        formResultRepository.deleteFormResult()

        //labTestTypeRepository.deleteAllLabTestType()

        workflowPhasesRepository.deleteAllWorkflowPhases()
        phaseComponentRepository.deleteAllPhaseComponents()
        componentFormJoinRepository.deleteAllComponentForms()



    }

    //Todo: remove  in production and sync with api
    private fun parseMetaData(retrofitResponseListener:RetrofitResponseListener,restCallback: RESTCallback) {

        try {
            getAllRelatedData(retrofitResponseListener)

            deleteExisitingLocalData()


            getWorkFlowFromAPI(retrofitResponseListener)
            getPhasesFromAPI(retrofitResponseListener)
            getComponentsFromAPI(retrofitResponseListener)
            getFormsFromAPI(retrofitResponseListener)



        } catch (e: JSONException) {
            e.printStackTrace()
            restCallback.onFailure(e)
        }
    }


    private fun skipLogicParser(sExpressionList: JSONArray): List<SExpression> {
        var SExpressionCompleteList = ArrayList<SExpression>()
        var sExpression: SExpression = SExpression()
        for (n in 0 until sExpressionList.length()) {
            val obj = sExpressionList.get(n)



            if (obj is String) {
                sExpression.operator = obj
            } else if (obj is JSONObject) {

                var s: SkipLogics = SkipLogics()
                //TODO QuestionId must be a string ~Taha
                val skiplogicID = obj.getString("id")
                val skiplogicQuestionId = obj.getInt("questionId")

                s.id = skiplogicID
                s.questionID = skiplogicQuestionId

                val skiplogicEqualList = obj.getJSONArray("equals")
                for (o in 0 until skiplogicEqualList.length()) {
                    val optionUUIDObject = skiplogicEqualList.getJSONObject(o)
                    val optionUUID = optionUUIDObject.getString("uuid")

                    s.equalsList.add(o, optionUUID)
                }

                val skiplogicNotEqualList = obj.getJSONArray("notEquals")
                for (o in 0 until skiplogicNotEqualList.length()) {
                    val optionUUIDObject = skiplogicNotEqualList.getJSONObject(o)
                    val optionUUID = optionUUIDObject.getString("uuid")

                    s.notEqualsList.add(o, optionUUID)
                }

                val skiplogicEqualsTo = obj.getJSONArray("equalTo")
                for (o in 0 until skiplogicEqualsTo.length()) {
                    val optionWithNumbers = skiplogicEqualsTo.getInt(o)

                    s.equalsToList.add(o, optionWithNumbers)
                }

                val skiplogicNotEqualsTo = obj.getJSONArray("notEqualTo")
                for (o in 0 until skiplogicNotEqualsTo.length()) {
                    val optionWithNumbers = skiplogicNotEqualsTo.getInt(o)

                    s.notEqualsToList.add(o, optionWithNumbers)
                }

                val skiplogicLessThan = obj.getJSONArray("lessThan")
                for (o in 0 until skiplogicLessThan.length()) {
                    val optionWithNumbers = skiplogicLessThan.getInt(o)

                    s.lessThanList.add(o, optionWithNumbers)
                }

                val skiplogicGreaterThan = obj.getJSONArray("greaterThan")
                for (o in 0 until skiplogicGreaterThan.length()) {
                    val optionWithNumbers = skiplogicGreaterThan.getInt(o)

                    s.greaterThanList.add(o, optionWithNumbers)
                }

                sExpression.skipLogicsObjects.add(s)
            } else if (obj is JSONArray) {
                sExpression.skipLogicsArray = skipLogicParser(obj)
            }


        }
        SExpressionCompleteList.add(sExpression)
        return SExpressionCompleteList
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