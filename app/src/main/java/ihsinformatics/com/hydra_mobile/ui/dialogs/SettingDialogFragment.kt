package ihsinformatics.com.hydra_mobile.ui.dialogs

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import ihsinformatics.com.hydra_mobile.data.repository.AppSettingRepository
import ihsinformatics.com.hydra_mobile.ui.viewmodel.AppSettingViewModel
import kotlinx.android.synthetic.main.app_setting.view.*

class SettingDialogFragment : DialogFragment() {

    private lateinit var appSettingViewModel: AppSettingViewModel
    private lateinit var repository: AppSettingRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.app_setting, container, false)
        dialog.window.attributes.windowAnimations = R.style.FullScreenDialogStyle
        view.img_close.setOnClickListener(View.OnClickListener {
            dismiss()
        })

        view.btn_save.setOnClickListener(View.OnClickListener {

            val ipAddress = view.edt_ip_address.text.toString()
            val portNumber = view.edt_port_number.text.toString()
            val ssl = view.cb_ssl_enable.isChecked

            saveAppSetting(ipAddress, portNumber,ssl)
            dismiss()
        })


        return view
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }

    companion object {

        fun newInstance(): SettingDialogFragment {
            return SettingDialogFragment()
        }
    }

    fun saveAppSetting(ipAddress: String, portNumber: String,sslEnable: Boolean) {
        appSettingViewModel = ViewModelProviders.of(this).get(AppSettingViewModel::class.java)
        appSettingViewModel.insert(
            AppSetting(
                ip = ipAddress,
                port = portNumber,
                ssl = sslEnable
            )
        )
        Toast.makeText(activity, "setting changed", Toast.LENGTH_SHORT).show()
    }

}