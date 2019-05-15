package ihsinformatics.com.hydra_mobile.view.fragments

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.persistentdata.entities.AppSetting
import ihsinformatics.com.hydra_mobile.repository.AppSettingRepository
import ihsinformatics.com.hydra_mobile.viewmodel.AppSettingViewModel
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

        val ipAddress = view.edt_ip_address.text.toString()
        val portNumber = view.edt_port_number.text.toString()

        view.btn_save.setOnClickListener(View.OnClickListener {
            saveAppSetting(ipAddress, portNumber)
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

    fun saveAppSetting(ipAddress: String, portNumber: String) {
        appSettingViewModel = ViewModelProviders.of(this).get(AppSettingViewModel::class.java)
        appSettingViewModel.insert(AppSetting(id = 0, ip = ipAddress, port = portNumber, ssl = true))
        Toast.makeText(activity, "setting changed", Toast.LENGTH_SHORT).show()
    }

}