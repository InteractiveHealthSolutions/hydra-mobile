package ihsinformatics.com.hydra_mobile.view.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.utils.AlertUtil
import ihsinformatics.com.hydra_mobile.view.widgets.InputWidget
import kotlinx.android.synthetic.main.activity_base.*
import timber.log.Timber
import java.util.ArrayList


abstract class BaseActivity : AppCompatActivity(), View.OnClickListener {


    override fun onClick(v: View?) {

    }

    private lateinit var progressDialog: ProgressDialog


    private var scrollPosition: Int = 0
    protected lateinit var inputWidgets: Map<Int, InputWidget>
    private var tvRatings: TextView? = null
    private var svQuestions: ScrollView? = null
    private var bitmap: Bitmap? = null
    private var llPatientInfoDisplayer: RelativeLayout? = null
    private var tvPatientName: TextView? = null
    private var tvAge: TextView? = null
    private var tvPatientLastName: TextView? = null
    private var tvPatientIdentifier: TextView? = null
    private var ivGender: ImageView? = null
    private var score: Int = 0
    var PATIENT_SESSION_NUMBER: String? = null

  /*  *//**
     * for initializing important variables in derived classes with uniformity.
     *//*
    abstract fun initVars()
    *//**
     * for defining layout file and initializing UI Components.
     *//*
    abstract fun initUI()*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager  .LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_base)
        initializeProgressDialog()
        btnSave.setOnClickListener(this)
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
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    /**
     * This method is responsible for initializing Progress Dialog
     */
    private fun initializeProgressDialog() {
        progressDialog = AlertUtil.getProgressDialog(this@BaseActivity)
        progressDialog.setMessage(getString(R.string.loding))
        progressDialog.setCancelable(false)
    }

    fun getProgressDialog(): ProgressDialog {
        return progressDialog
    }

    fun setProgressDialog(progressDialog: ProgressDialog) {
        this.progressDialog = progressDialog
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
}