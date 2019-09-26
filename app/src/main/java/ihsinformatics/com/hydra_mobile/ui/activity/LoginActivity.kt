package ihsinformatics.com.hydra_mobile.ui.activity


import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
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
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.local.entities.User
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.repository.WorkflowPhasesRepository
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import ihsinformatics.com.hydra_mobile.utils.SessionManager


class LoginActivity : BaseActivity(), View.OnClickListener {
    private lateinit var networkProgressDialog: NetworkProgressDialog
    lateinit var binding: ActivityLoginBinding
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_login -> {
                switchActivity()
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
                    RESTCallback {
                    override fun onFailure(t: Throwable) {
                        networkProgressDialog.dismiss()
                        Toast.makeText(this@LoginActivity, getString(R.string.authentication_error), Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun <T> onSuccess(o: T) {
                        SessionManager(applicationContext).createLoginSession(
                            usernameEditText.text.toString(),
                            passwordEditText.text.toString()
                        )
                        //Not required
                        Constant.currentUser = User(
                            username = usernameEditText.text.toString(),
                            password = passwordEditText.text.toString()
                        )

                        // getData()

                        networkProgressDialog.dismiss()
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
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
            usernameEditText.error = resources.getString(R.string.empty_field)
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

   /* private fun openDialog() {
        val dialog = AlertDialog.Builder(this)
            .setMessage(getString(R.string.are_you_sure_exit_application))
            .setTitle(getString(R.string.are_you_sure))
            .setNegativeButton(getString(R.string.no), null)
            .setPositiveButton(
                getString(R.string.yes)
            ) { _, _ ->
                //SessionManager(applicationContext).logoutUser()
                finishAffinity()
                *//*  System.exit(0)*//*
            }
        dialog.show()
    }
*/
    private fun getData() {
        val repo = WorkflowPhasesRepository(this.application)
        repo.getRemoteWorkflowData()
    }


}
