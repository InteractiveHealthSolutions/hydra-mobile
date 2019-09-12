package ihsinformatics.com.hydra_mobile

import android.app.Activity
import android.app.Application
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.*
import ihsinformatics.com.hydra_mobile.data.repository.*
import ihsinformatics.com.hydra_mobile.di.component.DaggerAppComponent
import org.json.JSONObject
import javax.inject.Inject
import java.io.IOException
import org.json.JSONException

/**
 * File Description
 * <p>
 * Author: shujaat ali
 * Email: shujaat.ali@ihsinformatics.com
 */


class HydraApp : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return dispatchingAndroidInjector
    }


    override fun onCreate() {
        super.onCreate()
        initializeAppSetting()
        parseMetaData()
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)


    }

    private fun initializeAppSetting() {
        val appSettingRepo = AppSettingRepository(this)
        appSettingRepo.insertSetting(
            AppSetting(
                getString(R.string.default_ip_address),
                getString(R.string.default_port_number),
                false
            )
        )
        // Toast.makeText(this, "saved default settings", Toast.LENGTH_SHORT).show()
    }

    private fun parseMetaData() {
        val phaseComponentFormJoinRepository = PhaseComponentFormJoinRepository(this)
        try {
            val obj = JSONObject(loadJSONFromAsset())
            val phasesArray = obj.getJSONArray("phases")

            for (i in 0 until phasesArray.length()) {
                val insidePhase = phasesArray.getJSONObject(i)
                val name = insidePhase.getString("name")
                val id = insidePhase.getInt("id")

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
                        FormRepository(this).insertForm(Forms(formId, formName, componentId, formName))
    //                          phaseComponentFormJoinRepository.insertComponentForm(
    //                            ComponentFormJoin(
    //                                componentId,
    //                                formId
    //                            )
    //                        )
                    }


                    ComponentRepository(this).insertComponent(Component(componentName, componentId, id))
                    phaseComponentFormJoinRepository.insert(PhasesComponentJoin(id, componentId))
                }

                PhaseRepository(this).insertPhase(Phases(name, id))
            }


        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    private fun loadJSONFromAsset(): String? {
        var json: String? = null
        try {
            val `is` = assets.open("workflow.json")
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

}