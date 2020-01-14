package ihsinformatics.com.hydra_mobile.ui.base

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.ui.activity.HomeActivity
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import timber.log.Timber


abstract class BaseActivity : AppCompatActivity(){


    private var defaultStartTransition = true
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)

        if (defaultStartTransition) {
            overridePendingTransitionEnter()
        } else {
            overridePendingTransitionExit()
        }
    }

    override fun onBackPressed() {
       /* val dialog = AlertDialog.Builder(this)
            .setMessage(getString(R.string.are_you_sure_exit_form))
            .setTitle(getString(R.string.are_you_sure))
            .setNegativeButton(getString(R.string.no), null)
            .setPositiveButton(
                getString(R.string.yes)
            ) { dialog, which -> super@BaseActivity.onBackPressed() }
        dialog.show()*/
    }

    /**
     * This method is responsible for showing Toast in center of the screen
     * @param message
     */
    fun showToast(message: String) {
        try {
            val toast = Toast.makeText(this@BaseActivity, message, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        } catch (e: Exception) {
            Timber.e("Error occurred while showToast(...), Error = $e")
        }

    }

    fun isInternetConnected(): Boolean {
        var isInternetConnected = false
        try {
            //            isInternetConnected = ((MyApplication)getApplicationContext()).isInternetConnected();
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            isInternetConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected
        } catch (e: Exception) {
            Timber.e("Error occurred while isInternetConnected(), Error = $e")
        }

        return isInternetConnected
    }

    /**
     * Has GPS enabled for the Device
     * @param mContext Application context
     * @return flag indicating the gps status
     */
    fun isGPSEnabled(mContext: Context): Boolean {
        var isGPSEnabled = false
        try {
            val locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            Timber.e("Error occurred while isGPSEnabled(...), Error = $e")
        }

        return isGPSEnabled
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    fun overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    fun overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    public fun openDialog() {
        val dialog = AlertDialog.Builder(this)
            .setMessage(getString(R.string.are_you_sure_exit_application))
            .setTitle(getString(R.string.are_you_sure))
            .setNegativeButton(getString(R.string.no), null)
            .setPositiveButton(
                getString(R.string.yes)
            ) { _, _ ->
                //SessionManager(applicationContext).logoutUser()
                finishAffinity()
                //
                //        dialog.show()System.exit(0)
            }
        dialog.show()
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


}