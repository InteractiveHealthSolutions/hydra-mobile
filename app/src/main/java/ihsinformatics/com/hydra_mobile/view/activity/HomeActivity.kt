package ihsinformatics.com.hydra_mobile.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.core.Form
import ihsinformatics.com.hydra_mobile.utils.ParamNames
import kotlinx.android.synthetic.main.activity_main.*


class HomeActivity : AppCompatActivity(), View.OnClickListener {
    var encounterName: String? = null
    override fun onClick(p0: View?) {
        encounterName = ParamNames.ENCOUNTER_TYPE_PATIENT_CREATION
        startActivity(Intent(this, Form::class.java))
      //  Form.setENCOUNTER_NAME(encounterName)
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        img_back.setOnClickListener(this)
    }


}
