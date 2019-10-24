package ihsinformatics.com.hydra_mobile.ui.activity


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import ihsinformatics.com.hydra_mobile.databinding.ActivityLoginBinding
import ihsinformatics.com.hydra_mobile.data.repository.UserRepository
import ihsinformatics.com.hydra_mobile.utils.KeyboardUtil
import ihsinformatics.com.hydra_mobile.ui.dialogs.NetworkProgressDialog
import ihsinformatics.com.hydra_mobile.ui.dialogs.SettingDialogFragment
import android.widget.Toast
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.local.entities.User
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.services.manager.MetaDataHelper
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import android.app.Activity
import android.widget.CheckBox
import com.ihsinformatics.dynamicformsgenerator.screens.Form
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.core.question.config.QuestionConfiguration


class LoginActivity : BaseActivity(), View.OnClickListener {


    private lateinit var networkProgressDialog: NetworkProgressDialog
    lateinit var binding: ActivityLoginBinding
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var checkboxRemember:CheckBox

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

    private fun startSampleForm() {
        var encounterName = "Patient Registration"
        startActivity(Intent(this, Form::class.java))
        Form.setENCOUNTER_NAME(encounterName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        usernameEditText = binding.edtUsername;
        passwordEditText = binding.edtPassword
        checkboxRemember=binding.checkRemember
        binding.btnLogin.setOnClickListener(this)
        binding.imgSetting.setOnClickListener(this)
        networkProgressDialog = NetworkProgressDialog(this)

    }


    private fun switchActivity() {
        if (validation()) {
            networkProgressDialog.show()
            KeyboardUtil.hideSoftKeyboard(this@LoginActivity)

            UserRepository(application).userAuthentication(
                usernameEditText.text.toString(),
                passwordEditText.text.toString(),
                object :
                    RESTCallback {    //TODO Apply proper error message for e.g if server is down then show that
                    override fun onFailure(t: Throwable) {
                        networkProgressDialog.dismiss()
                        Toast.makeText(this@LoginActivity, getString(R.string.authentication_error), Toast.LENGTH_SHORT)
                            .show()
                        onSuccess(t)
                    }

                    override fun <T> onSuccess(o: T) {
                        if(checkboxRemember.isChecked)
                        {
                            SessionManager(applicationContext).createLoginSession(
                            usernameEditText.text.toString(),
                            passwordEditText.text.toString()
                        )
                        }
//                        SessionManager(applicationContext).createLoginSession(
//                            usernameEditText.text.toString(),
//                            passwordEditText.text.toString()
//                        )
                        //Not required
                        Constant.currentUser = User(
                            username = usernameEditText.text.toString(),
                            password = passwordEditText.text.toString()
                        )

                        networkProgressDialog.dismiss()


                        openMetaDataFetcher()


                    }
                })
        }
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
        val metaDataHelper = MetaDataHelper(this)
        metaDataHelper.getAllMetaData(object : RESTCallback {
            override fun <T> onSuccess(o: T) {
                val isSuccess = o as Boolean
                if (isSuccess) {
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finish()

                }

            }

            override fun onFailure(t: Throwable) {

            }
        })


    }


}