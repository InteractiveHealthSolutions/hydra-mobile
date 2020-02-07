package ihsinformatics.com.hydra_mobile.ui.activity


import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import ihsinformatics.com.hydra_mobile.databinding.ActivityLoginBinding
import ihsinformatics.com.hydra_mobile.data.repository.UserRepository
import ihsinformatics.com.hydra_mobile.utils.KeyboardUtil
import ihsinformatics.com.hydra_mobile.ui.dialogs.NetworkProgressDialog
import android.widget.Toast
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.services.manager.MetaDataHelper
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import android.os.Handler
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.CheckBox
import androidx.lifecycle.ViewModelProviders
import com.ihsinformatics.dynamicformsgenerator.screens.Form
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget

import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.user.ProviderApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.service.ProviderApiService
import ihsinformatics.com.hydra_mobile.ui.dialogs.SettingDialogFragment
import ihsinformatics.com.hydra_mobile.ui.viewmodel.PhaseComponentJoinViewModel
import ihsinformatics.com.hydra_mobile.ui.viewmodel.UserViewModel
import ihsinformatics.com.hydra_mobile.ui.viewmodel.WorkFlowViewModel
import ihsinformatics.com.hydra_mobile.ui.viewmodel.WorkflowPhasesMapViewModel
import ihsinformatics.com.hydra_mobile.utils.GlobalPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : BaseActivity(), View.OnClickListener {


    private lateinit var networkProgressDialog: NetworkProgressDialog
    lateinit var binding: ActivityLoginBinding
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var checkboxOffline: CheckBox
    private var showPassword = 0

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


            if (isInternetConnected()) {
                UserRepository(application).userAuthentication(usernameEditText.text.toString(), passwordEditText.text.toString(), object : RESTCallback {    //TODO Apply proper error message for e.g if server is down then show that
                    override fun onFailure(t: Throwable) {
                        networkProgressDialog.dismiss()
                        if (t.localizedMessage.toString().equals(getString(R.string.authentication_error))) ToastyWidget.getInstance().displayError(this@LoginActivity, getString(R.string.authentication_error), Toast.LENGTH_SHORT)
                        else ToastyWidget.getInstance().displayError(this@LoginActivity, getString(R.string.internet_issue), Toast.LENGTH_SHORT)
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

                if (users != null && users.size > 0 && users[0].username.toLowerCase().equals(usernameEditText.text.toString().toLowerCase()) && users[0].password.equals(passwordEditText.text.toString())) {

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
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finish()

                }

            }

            override fun onFailure(t: Throwable) {

            }
        })


    }


}