package ihsinformatics.com.hydra_mobile.view.activity

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import es.dmoral.toasty.Toasty
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.PatientData
import ihsinformatics.com.hydra_mobile.utils.AlertUtil
import ihsinformatics.com.hydra_mobile.utils.ParamNames
import ihsinformatics.com.hydra_mobile.utils.Translator
import ihsinformatics.com.hydra_mobile.utils.ValidationError
import ihsinformatics.com.hydra_mobile.view.dialogs.NetworkProgressDialog
import ihsinformatics.com.hydra_mobile.view.widgets.InputWidget
import ihsinformatics.com.hydra_mobile.view.widgets.QRReaderWidget
import ihsinformatics.com.hydra_mobile.view.widgets.ScoreSpinner
import ihsinformatics.com.hydra_mobile.view.widgets.SpinnerWidget
import ihsinformatics.com.hydra_mobile.view.widgets.image.ImageWidget
import kotlinx.android.synthetic.main.activity_base.*
import timber.log.Timber
import java.util.ArrayList


abstract class BaseActivity : AppCompatActivity(), View.OnClickListener {

    //Todo : Clean code
    override fun onClick(v: View?) {
        if (v != null) {
            if (v.id == R.id.btnSave) {
                AlertDialog.Builder(this@BaseActivity)
                    .setTitle(getString(R.string.do_you_want_to_save))
                    .setMessage(getString(R.string.do_you_want_to_save))
                    .setNegativeButton(getString(R.string.yes), null)
                    .setPositiveButton(getString(R.string.no)) { arg0, arg1 -> saveForm() }.show()
            }
        }
    }

    private var scrollPosition: Int = 0
    lateinit var inputWidgets: Map<Int, InputWidget>
    private lateinit var tvRatings: TextView
    private lateinit var svQuestions: ScrollView
    private lateinit var bitmap: Bitmap
    private lateinit var llPatientInfoDisplayer: RelativeLayout
    private lateinit var tvPatientName: TextView
    private lateinit var tvAge: TextView
    private lateinit var tvPatientLastName: TextView
    private lateinit var tvPatientIdentifier: TextView
    private lateinit var ivGender: ImageView
    private var score: Int = 0
    private lateinit var PATIENT_SESSION_NUMBER: String
    private lateinit var networkProgressDialog: NetworkProgressDialog
    private lateinit var errors: ArrayList<ValidationError>
    lateinit var patientData: PatientData
    private var defaultStartTransition = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_base)
        networkProgressDialog = NetworkProgressDialog(this)
        errors = ArrayList<ValidationError>()
        tvPatientName = findViewById(R.id.tvName) as TextView
        tvPatientLastName = findViewById(R.id.tvLastName) as TextView
        tvAge = findViewById<TextView>(R.id.tvAge)
        tvPatientIdentifier = findViewById<TextView>(R.id.tvId)
        ivGender = findViewById<ImageView>(R.id.ivGender)
        btnSave.setOnClickListener(this)
    }

    fun saveForm() {
    }

    //Todo : add reason ?
    fun onChildViewItemSelected(showables: IntArray?, hideables: IntArray?) {
        if (showables != null) {
            for (i in showables) {
                val iw = inputWidgets[i]
                if (iw != null) {
                    iw.visibility = View.VISIBLE
                }
            }
        }
        if (hideables != null) {
            for (i in hideables) {
                val iw = inputWidgets[i]
                if (iw != null) {
                    iw.visibility = View.GONE
                }
            }
        }
    }

    fun addValidationError(questionId: Int, cause: String) {
        errors.add(ValidationError(questionId, cause))
    }

    // TODO handle some form type specific tasks
    fun handleEncounterType() {

    }

    fun fillPatientInfoBar() {

        tvPatientName.setText(patientData.patient.givenName)
        tvPatientLastName.setText(patientData.patient.familyName)
        tvAge.setText(patientData.patient.age) //TODO get dob and display full age till days
        tvPatientIdentifier.setText(patientData.patient.identifier)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (patientData.patient.gender.toLowerCase().startsWith("m")) {

                ivGender.setImageDrawable(getDrawable(R.drawable.male_icon))

            } else {
                ivGender.setImageDrawable(getDrawable(R.drawable.female_icon))
            }
        }
    }

    fun addInRating(i: Int) {
        score += i
        if (FormActivity.ENCOUNTER_NAME.equals(ParamNames.ENCOUNTER_TYPE_MEDICAL_QUESTIONAIRE)) {
            tvRatings.visibility = View.GONE
            val sw = inputWidgets[65] as ScoreSpinner?
            if (sw != null) {
                sw!!.setselectedIndex(score)
            }
        } else if (FormActivity.ENCOUNTER_NAME.equals(ParamNames.ENCOUNTER_TYPE_SCREENING)) {
            tvRatings.visibility = View.GONE
            val ew = inputWidgets[126] as SpinnerWidget?
            if (ew != null) {
                ew!!.setAnswer(score.toString() + "", "", Translator.LANGUAGE.ENGLISH)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val w = inputWidgets[requestCode]
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (w is ImageWidget) {
                val i = w as ImageWidget?
                if (permissions[0] == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    i!!.startCamera()
                } else if (permissions[0] == Manifest.permission.CAMERA) {
                    i!!.callCamera()
                }
            } else if (w is QRReaderWidget) {
                val q = w as QRReaderWidget?
                if (permissions[0] == Manifest.permission.CAMERA) {
                    q!!.showQRCodeReaderDialog()
                }
            }
        } else {
            Toasty.info(this, "Permission denied", Toast.LENGTH_LONG).show()
        }

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
        val dialog = AlertDialog.Builder(this)
            .setMessage(getString(R.string.are_you_sure_exit_form))
            .setTitle(getString(R.string.are_you_sure))
            .setNegativeButton(getString(R.string.no), null)
            .setPositiveButton(
                getString(R.string.yes)
            ) { dialog, which -> super@BaseActivity.onBackPressed() }
        dialog.show()
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
    protected fun overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected fun overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }
}