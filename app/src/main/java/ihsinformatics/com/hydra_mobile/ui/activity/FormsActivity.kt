package ihsinformatics.com.hydra_mobile.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity;
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant

import kotlinx.android.synthetic.main.activity_patient_information.*
import kotlinx.android.synthetic.main.activity_patient_information.toolbar

class FormsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = ""
        setContentView(R.layout.activity_patient_information)
        collapsingToolbar.title = Constant.formName
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        initView()
        setupWindowAnimations()
    }

    private fun setupWindowAnimations() {

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)

    }


    private fun initView() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                openDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        openDialog()
        //super.onBackPressed()

    }

    fun openDialog() {
        val dialog = AlertDialog.Builder(this)
            .setMessage(getString(R.string.are_you_sure_exit_form))
            .setTitle(getString(R.string.are_you_sure))
            .setNegativeButton(getString(R.string.no), null)
            .setPositiveButton(
                getString(R.string.yes)
            ) { _, _ ->
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        dialog.show()
    }

}
