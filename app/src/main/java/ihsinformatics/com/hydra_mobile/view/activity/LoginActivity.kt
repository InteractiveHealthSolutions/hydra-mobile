package ihsinformatics.com.hydra_mobile.view.activity



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import ihsinformatics.com.hydra_mobile.databinding.ActivityLoginBinding
import ihsinformatics.com.hydra_mobile.repository.UserRepository
import ihsinformatics.com.hydra_mobile.utils.KeyboardUtil
import ihsinformatics.com.hydra_mobile.view.dialogs.NetworkProgressDialog
import ihsinformatics.com.hydra_mobile.view.dialogs.SettingDialogFragment
import android.widget.Toast
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback


class LoginActivity : AppCompatActivity(), View.OnClickListener {
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
                        //SnackBarUtil.getSnackBar(this, getString(R.string.authentication_error), "#BE1F18").build()
                    }

                    override fun onSuccess(o: Any) {
                        networkProgressDialog.dismiss()
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
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


}
