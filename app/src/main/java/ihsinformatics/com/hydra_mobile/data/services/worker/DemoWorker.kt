package ihsinformatics.com.hydra_mobile.data.services.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.*
import ihsinformatics.com.hydra_mobile.data.repository.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

class DemoWorker (context: Context, workerParams: WorkerParameters) : Worker(context, workerParams){


    lateinit var context:Context

    init {
        this.context=context
    }


    override fun doWork(): Result =try {

        initializeAppSetting()
        parseMetaData()
        Log.e(LOG_TAG, "Work Complete")

        Result.success()
    } catch (e: Throwable) {
        Log.e(LOG_TAG, "Error executing work: " + e.message, e)
        Result.failure()
    }



    private fun initializeAppSetting() {
        val appSettingRepo = AppSettingRepository(context)
        appSettingRepo.insertSetting(
            AppSetting(
                context.getString(R.string.default_ip_address),
                context.getString(R.string.default_port_number),
                false
            )
        )
        // Toast.makeText(this, "saved default settings", Toast.LENGTH_SHORT).show()
    }


    //Todo: remove  in production and sync with api
    private fun parseMetaData() {
        val phaseComponentFormJoinRepository = PhaseComponentFormJoinRepository(context)
        try {
            val completeFile = JSONArray(loadJSONFromAsset())

            for(j in 0 until completeFile.length())
            {
                val workflow=completeFile.getJSONObject(j)
                val workFlowId=workflow.getInt("id")
                val workflowName= workflow.getString("name")
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
                            FormRepository(context).insertForm(Forms(formId, formName, componentId, formName))
                            //                          phaseComponentFormJoinRepository.insertComponentForm(
                            //                            ComponentFormJoin(
                            //                                componentId,
                            //                                formId
                            //                            )
                            //                        )
                        }


                        ComponentRepository(context).insertComponent(Component(componentName, componentId))
                        phaseComponentFormJoinRepository.insert(PhasesComponentJoin(phaseId, componentId))
                    }

                  //  PhaseRepository(context).insertPhase(Phases(phaseName, phaseId))
                    //WorkFlowPhasesJoinRepository(context).insert(WorkFlowPhasesJoin(phaseId, workFlowId))

                }
               // WorkFlowRepository(context).insertWorkFlow(WorkFlow(workFlowId,workflowName))
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }

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