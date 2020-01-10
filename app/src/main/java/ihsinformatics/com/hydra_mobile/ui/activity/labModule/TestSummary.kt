package ihsinformatics.com.hydra_mobile.ui.activity.labModule

import android.content.Intent
import android.os.Bundle
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.ui.activity.HomeActivity
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity

class TestSummary : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_summary)
    }

    override fun onBackPressed() {

        startActivity(Intent(this, CommonLabActivity::class.java))
        finish()
        super.onBackPressed()
    }

}
