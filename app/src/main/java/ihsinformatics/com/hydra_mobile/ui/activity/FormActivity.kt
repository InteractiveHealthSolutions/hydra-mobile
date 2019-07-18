package ihsinformatics.com.hydra_mobile.ui.activity

import android.os.Bundle
import android.widget.LinearLayout
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import ihsinformatics.com.hydra_mobile.ui.widgets.InputWidgetProvider

class FormActivity : BaseActivity() {

    val paramFormId = "formId"

    companion object {
        lateinit var ENCOUNTER_NAME: String
        lateinit var REGISTRATION_ENCOUNTER: String
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val provider = InputWidgetProvider.getInstance()
        val llMain = findViewById(R.id.base_form_container) as LinearLayout

        val intent = intent
        if (REGISTRATION_ENCOUNTER == null) {
            throw UnsupportedOperationException("You need to set value of static variable REGISTRATION_ENCOUNTER in DataProvider class") as Throwable
        }

        title = ENCOUNTER_NAME
        var loadData = intent.getBooleanExtra(Constant.KEY_LOAD_DATA, false)
        var data = intent.getStringExtra(Constant.KEY_JSON_DATA)

    }

    fun getEncounterName(): String {
        return ENCOUNTER_NAME
    }

    fun setEncounterName(encounterName: String) {
        ENCOUNTER_NAME = encounterName
    }


}