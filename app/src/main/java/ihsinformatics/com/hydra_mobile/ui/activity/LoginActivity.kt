package ihsinformatics.com.hydra_mobile.ui.activity


import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.repository.UserRepository
import ihsinformatics.com.hydra_mobile.data.services.manager.MetaDataHelper
import ihsinformatics.com.hydra_mobile.databinding.ActivityLoginBinding
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import ihsinformatics.com.hydra_mobile.ui.dialogs.NetworkProgressDialog
import ihsinformatics.com.hydra_mobile.ui.dialogs.SettingDialogFragment
import ihsinformatics.com.hydra_mobile.ui.viewmodel.UserViewModel
import ihsinformatics.com.hydra_mobile.ui.viewmodel.WorkFlowViewModel
import ihsinformatics.com.hydra_mobile.utils.GlobalPreferences
import ihsinformatics.com.hydra_mobile.utils.KeyboardUtil
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class LoginActivity : BaseActivity(), View.OnClickListener {


    private lateinit var networkProgressDialog: NetworkProgressDialog
    lateinit var binding: ActivityLoginBinding
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var checkboxOffline: CheckBox
    private var showPassword = 0
    private lateinit var languagesSpinner: Spinner

    init {

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_login -> {
                // startSampleForm()
                switchActivity()
                // openMetaDataFetcher()
            }
            R.id.img_setting -> openSettingDialog()
            else -> print("")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        usernameEditText = binding.edtUsername;
        passwordEditText = binding.edtPassword
        checkboxOffline = binding.checkOfflineMode
        binding.btnLogin.setOnClickListener(this)
        binding.imgSetting.setOnClickListener(this)
        networkProgressDialog = NetworkProgressDialog(this)

        languagesSpinner = binding.language

        var listOfLanguages= arrayListOf<String>()
        listOfLanguages.add("English")
        listOfLanguages.add("Bhasa")


        var adapter = ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,listOfLanguages)

        languagesSpinner.adapter=adapter

        flipit(binding.ivLogo)

        binding.loginEye.setOnClickListener { view ->

            if (showPassword == 0) {
                binding.loginEye.setImageDrawable(resources.getDrawable(R.drawable.login_show_eye))
                showPassword = 1
                binding.edtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()

            } else {
                binding.loginEye.setImageDrawable(resources.getDrawable(R.drawable.login_cross_eye))
                showPassword = 0
                binding.edtPassword.transformationMethod = PasswordTransformationMethod.getInstance()

            }
        }

    }

    private fun flipit(viewToFlip: View) {
        val flip = ObjectAnimator.ofFloat(viewToFlip, "rotationY", 0f, 360f)
        flip.duration = 3500
        flip.start()

        val secondsDelayed = 1
        Handler().postDelayed({
            flipit(viewToFlip)
        }, (secondsDelayed * 4500).toLong())

    }

    private fun switchActivity() {
        if (validation()) {
            networkProgressDialog.show()
            KeyboardUtil.hideSoftKeyboard(this@LoginActivity)

            //setLocale(this, languagesSpinner.selectedItem.toString())
            val lang=languageConverter( languagesSpinner.selectedItem.toString())
            updateResources(this,lang)

            if (isInternetConnected()) {
                UserRepository(application).userAuthentication(usernameEditText.text.toString(), passwordEditText.text.toString(), object : RESTCallback {    //TODO Apply proper error message for e.g if server is down then show that
                    override fun onFailure(t: Throwable) {
                        networkProgressDialog.dismiss()
                        if (t.localizedMessage.toString().equals(getString(R.string.authentication_error))) ToastyWidget.getInstance().displayError(this@LoginActivity, getString(R.string.authentication_error), Toast.LENGTH_SHORT)
                        else ToastyWidget.getInstance().displayError(this@LoginActivity, getString(R.string.internet_or_server_error), Toast.LENGTH_SHORT)
                    }

                    override fun <T> onSuccess(o: T) {


                        if (checkboxOffline.isChecked) {
                            SessionManager(applicationContext).enableOfflineMode()

                        } else {
                            SessionManager(applicationContext).disableOfflineMode()
                        }
                        var provider = GlobalPreferences.getinstance(application).findPrferenceValue(GlobalPreferences.KEY.PROVIDER, null)

                        SessionManager(applicationContext).createLoginSession(usernameEditText.text.toString(), passwordEditText.text.toString(), provider)
                        GlobalPreferences.getinstance(this@LoginActivity).addOrUpdatePreference(GlobalPreferences.KEY.USERNAME, usernameEditText.text.toString())
                        GlobalPreferences.getinstance(this@LoginActivity).addOrUpdatePreference(GlobalPreferences.KEY.PASSWORD, passwordEditText.text.toString())

                        networkProgressDialog.dismiss()


                        if (isInternetConnected()) {
                            openMetaDataFetcher()
                        } else if (SessionManager(applicationContext).isFirstTime()) {
                            ToastyWidget.getInstance().displayError(this@LoginActivity, getString(R.string.internet_issue), Toast.LENGTH_LONG)
                        } else {
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            finish()
                        }

                        GlobalPreferences.getinstance(this@LoginActivity).addOrUpdatePreference(GlobalPreferences.KEY.WORKFLOW, null)
                        GlobalPreferences.getinstance(this@LoginActivity).addOrUpdatePreference(GlobalPreferences.KEY.WORKFLOWUUID, null)


                    }
                })
            } else {
                //fetch patient from database
                networkProgressDialog.dismiss()

                val userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

                val users = userViewModel.getUserByUsernameAndPass(usernameEditText.text.toString().toLowerCase(), passwordEditText.text.toString())

                if (users != null && users.size > 0 && users[0].username.toLowerCase().equals(usernameEditText.text.toString().toLowerCase()) && users[0].password.equals(passwordEditText.text.toString()) && users[0].provider!=null) {


                    SessionManager(applicationContext).createLoginSession(usernameEditText.text.toString(), passwordEditText.text.toString(),  users[0].provider)
                    GlobalPreferences.getinstance(this@LoginActivity).addOrUpdatePreference(GlobalPreferences.KEY.USERNAME, usernameEditText.text.toString())
                    GlobalPreferences.getinstance(this@LoginActivity).addOrUpdatePreference(GlobalPreferences.KEY.PASSWORD, passwordEditText.text.toString())


                    GlobalPreferences.getinstance(this@LoginActivity).addOrUpdatePreference(GlobalPreferences.KEY.WORKFLOW, null)
                    GlobalPreferences.getinstance(this@LoginActivity).addOrUpdatePreference(GlobalPreferences.KEY.WORKFLOWUUID, null)

                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finish()
                } else {
                    ToastyWidget.getInstance().displayError(this,getString(R.string.no_offline_user),Toast.LENGTH_LONG)
                }
            }
        }
    }


    //checks if workflows exist in offline db and return boolean accordingly
    private fun doesWorkflowExsist(): Boolean {

        val workflowViewModel = ViewModelProviders.of(this).get(WorkFlowViewModel::class.java)

        val work = workflowViewModel.getAllWorkflow()

        if (work.size != 0) {
            return true
        }

        return false
    }

    private fun openSettingDialog() {

        val fragmentManager = supportFragmentManager.beginTransaction()
        val settingFragment = SettingDialogFragment.newInstance()
        settingFragment.show(fragmentManager, "settingDialog");
    }

    private fun validation(): Boolean {
        var isValidated = true;
        if (usernameEditText.text.isBlank() || usernameEditText.text.isEmpty()) {
            usernameEditText.error = resources.getString(ihsinformatics.com.hydra_mobile.R.string.empty_field)
            usernameEditText.requestFocus()
            isValidated = false
        } else if (passwordEditText.text.isBlank() || passwordEditText.text.isEmpty()) {
            passwordEditText.error = resources.getString(R.string.empty_field)
            passwordEditText.requestFocus()
            isValidated = false
        }

        return isValidated
    }

    override fun onResume() {
        super.onResume()
        networkProgressDialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        networkProgressDialog.dismiss()
    }

    override fun onBackPressed() {
        openDialog()
    }


    private fun openMetaDataFetcher() {
        networkProgressDialog.show()
        val metaDataHelper = MetaDataHelper(this)
        metaDataHelper.getAllMetaData(object : RESTCallback {
            override fun <T> onSuccess(o: T) {
                val isSuccess = o as Boolean
                if (isSuccess) {
                    networkProgressDialog.dismiss()

                   //saveSessionStartTime()
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finish()

                }

            }

            override fun onFailure(t: Throwable) {
                networkProgressDialog.dismiss()
                ToastyWidget.getInstance().displayError(this@LoginActivity,getString(R.string.network_problem),Toast.LENGTH_LONG)
            }
        })


    }


    fun saveSessionStartTime()
    {
//        val simpleDateFormat = SimpleDateFormat("hh:mm a")
//
//        val currentTime = simpleDateFormat.parse(Calendar.getInstance().time.time.toString())
//        GlobalPreferences.getinstance(this@LoginActivity).addOrUpdatePreference(GlobalPreferences.KEY.ACTIVE_TIME, currentTime.toString())

        val cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"))
        val currentLocalTime = cal.time
        val date: DateFormat = SimpleDateFormat("HH:mm")

        date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"))

        val localTime: String = date.format(currentLocalTime)

        GlobalPreferences.getinstance(this@LoginActivity).addOrUpdatePreference(GlobalPreferences.KEY.ACTIVE_TIME, localTime)
    }


    private fun updateResources(context: Context, language: String): Boolean {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources: Resources = context.getResources()
        val configuration: Configuration = resources.getConfiguration()
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.getDisplayMetrics())
        GlobalPreferences.getinstance(this@LoginActivity).addOrUpdatePreference(GlobalPreferences.KEY.APP_LANGUAGE,language)
        Global.APP_LANGUAGE=language

        return true
    }


    private fun setLocale(context:Context,language: String) {
        val resources: Resources = context.getResources()
        val dm=resources.getDisplayMetrics()
        val configuration: Configuration = resources.getConfiguration()

        configuration.setLocale(Locale(language.toLowerCase()))

        //resources.updateConfiguration(configuration,dm)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            context.createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration, context.getResources().getDisplayMetrics());
        }
        GlobalPreferences.getinstance(this@LoginActivity).addOrUpdatePreference(GlobalPreferences.KEY.APP_LANGUAGE,language)
        Global.APP_LANGUAGE=language
    }


    private fun languageConverter(language:String):String
    {
        var lang="en"
        when(language.toLowerCase())
        {
            "english" -> lang="en"
            "bhasa" -> lang="in"

            else -> lang="en"
        }

        return lang;
    }
}