package ihsinformatics.com.hydra_mobile.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_common_lab.*

class CommonLabActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = ""
        setContentView(R.layout.activity_common_lab)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
        super.onBackPressed()

    }
}