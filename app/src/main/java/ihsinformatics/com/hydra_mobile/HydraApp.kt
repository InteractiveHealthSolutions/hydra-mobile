package ihsinformatics.com.hydra_mobile

import android.app.Application
import android.content.Context
import android.widget.Toast
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.*
import ihsinformatics.com.hydra_mobile.data.repository.*
import org.json.JSONArray
import java.io.IOException
import org.json.JSONException

/**
 * File Description
 * <p>
 * Author: shujaat ali
 * Email: shujaat.ali@ihsinformatics.com
 */


class HydraApp : Application() {
    
    companion object {
        var context: Context? = null;
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext;
    }
//
//    private fun initializeAppSetting() {
//        val appSettingRepo = AppSettingRepository(this)
//        appSettingRepo.insertSetting(
//            AppSetting(
//                getString(R.string.default_ip_address),
//                getString(R.string.default_port_number),
//                false
//            )
//        )
//        // Toast.makeText(this, "saved default settings", Toast.LENGTH_SHORT).show()
//    }
//
//
//    //Todo: remove  in production and sync with api
//    private fun parseMetaData() {
//        val phaseComponentFormJoinRepository = PhaseComponentFormJoinRepository(this)
//        val componentFormJoinRepository= ComponentFormJoinRepository(this)
//        try {
//            val completeFile = JSONArray(loadJSONFromAsset())
//
//            for(j in 0 until completeFile.length())
//            {
//                val workflow=completeFile.getJSONObject(j)
//                val workFlowId=workflow.getInt("id")
//                val workflowName= workflow.getString("name")
//                val phasesArray = workflow.getJSONArray("phases")
//
//                for (i in 0 until phasesArray.length()) {
//                    val insidePhase = phasesArray.getJSONObject(i)
//                    val phaseName = insidePhase.getString("name")
//                    val phaseId = insidePhase.getInt("id")
//
//                    val components = insidePhase.getJSONArray("components")
//                    for (j in 0 until components.length()) {
//                        val insideComponent = components.getJSONObject(j)
//                        val componentName = insideComponent.getString("name")
//                        val componentId = insideComponent.getInt("id")
//                        val formList = insideComponent.getJSONArray("forms")
//                        for (k in 0 until formList.length()) {
//                            val insideForm = formList.getJSONObject(k)
//                            val formName = insideForm.getString("encounterType")
//                            val formId = insideForm.getInt("formId")
//                            FormRepository(this).insertForm(Forms(formId, formName, componentId, formName))
//                            componentFormJoinRepository.insert(ComponentFormJoin(componentId,formId))
//                        }
//
//
//                        ComponentRepository(this).insertComponent(Component(componentName, componentId))
//                        phaseComponentFormJoinRepository.insert(PhasesComponentJoin(phaseId, componentId))
//                    }
//
//                    PhaseRepository(this).insertPhase(Phases(phaseName, phaseId))
//                    WorkFlowPhasesJoinRepository(this).insert(WorkFlowPhasesJoin(workFlowId,phaseId))
//
//
//                }
//                WorkFlowRepository(this).insertWorkFlow(WorkFlow(workFlowId,workflowName))
//
//            }
//            Toast.makeText(applicationContext,"Chala",Toast.LENGTH_SHORT).show()
//
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//
//    }
//
//    private fun loadJSONFromAsset(): String? {
//        var json: String? = null
//        try {
//            val `is` = assets.open("workflow.json")
//            val size = `is`.available()
//            val buffer = ByteArray(size)
//            `is`.read(buffer)
//            `is`.close()
//            json = String(buffer)
//        } catch (ex: IOException) {
//            ex.printStackTrace()
//            return json
//        }
//        return json
//    }


}