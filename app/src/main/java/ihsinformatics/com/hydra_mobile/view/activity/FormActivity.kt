package ihsinformatics.com.hydra_mobile.view.activity

import android.os.Bundle
import android.widget.LinearLayout
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.view.widgets.InputWidgetProvider

class FormActivity : BaseActivity() {
    //static object Todo
    val PARAM_FORM_ID = "formId"
    private var ENCOUNTER_NAME: String? = null
    var REGISTRATION_ENCOUNTER: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val provider = InputWidgetProvider.getInstance()
        val llMain = findViewById(R.id.llMain) as LinearLayout

        val intent = intent
        if (REGISTRATION_ENCOUNTER == null) {
            throw UnsupportedOperationException("You need to set value of static variable REGISTRATION_ENCOUNTER in DataProvider class")
        }

        title = ENCOUNTER_NAME
        var loadData = intent.getBooleanExtra(Constant.KEY_LOAD_DATA, false)
        var data = intent.getStringExtra(Constant.KEY_JSON_DATA)

    }


}