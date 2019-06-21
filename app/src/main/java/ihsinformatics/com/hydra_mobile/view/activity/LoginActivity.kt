package ihsinformatics.com.hydra_mobile.view.activity


import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.repository.UserRepository
import ihsinformatics.com.hydra_mobile.utils.ProgressDialog
import ihsinformatics.com.hydra_mobile.view.dialogs.NetworkProgressDialog
import ihsinformatics.com.hydra_mobile.view.fragments.SettingDialogFragment
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_setting_dialog.*
import org.jetbrains.anko.design.snackbar


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var networkProgressDialog: NetworkProgressDialog

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_login -> switchActivity()
            R.id.img_setting -> openSettingDialog()
            else -> print("")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login.setOnClickListener(this)
        img_setting.setOnClickListener(this)
        networkProgressDialog = NetworkProgressDialog(this)

    }

    private fun switchActivity() {
        if (validation()) {
            networkProgressDialog.show()
            startActivity(Intent(this, MainMenu::class.java))
            finish()
            /*       val repository = UserRepository(application)
                   val isAuthenticateUser =
                       repository.userAuthentication(edt_username.text.toString(), edt_password.text.toString())
                   if (isAuthenticateUser) {
                       startActivity(Intent(this, HomeActivity::class.java))
                   } else {
                       view.snackbar(R.string.authentication_error)
                   }*/
        }
    }

    private fun openSettingDialog() {

        val fragmentManager = supportFragmentManager.beginTransaction()
        val settingFragment = SettingDialogFragment.newInstance()
        settingFragment.show(fragmentManager, "settingDialog");
    }

    private fun validation(): Boolean {
        var isValidated = true;
        if (edt_username.text.isBlank() || edt_username.text.isEmpty()) {
            edt_username.error = resources.getString(R.string.empty_field)
            edt_username.requestFocus()
            isValidated = false
        } else if (edt_password.text.isBlank() || edt_password.text.isEmpty()) {
            edt_password.error = resources.getString(R.string.empty_field)
            edt_password.requestFocus()
            isValidated = false
        }

        return isValidated
    }


}
