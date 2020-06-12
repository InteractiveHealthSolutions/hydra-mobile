package ihsinformatics.com.hydra_mobile.ui.dialogs

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import ihsinformatics.com.hydra_mobile.data.repository.AppSettingRepository
import ihsinformatics.com.hydra_mobile.ui.viewmodel.AppSettingViewModel
import kotlinx.android.synthetic.main.display_setting.view.cb_ssl_enable
import kotlinx.android.synthetic.main.display_setting.view.edt_ip_address
import kotlinx.android.synthetic.main.display_setting.view.edt_port_number
import kotlinx.android.synthetic.main.display_setting.view.img_close
import kotlinx.android.synthetic.main.display_setting.view.*
import kotlinx.coroutines.runBlocking

class DisplaySettingsDialogFragment : DialogFragment() {

    private lateinit var appSettingViewModel: AppSettingViewModel
    private lateinit var repository: AppSettingRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.display_setting, container, false)
        dialog!!.window.attributes.windowAnimations = R.style.FullScreenDialogStyle

        var repository = context?.let { AppSettingRepository(it) }
        runBlocking {
            var list = repository!!.getSettingList()
            if (list.isNotEmpty()) {
                val setting: AppSetting = list.get(0)

                if (null != setting.ip && !setting.ip.equals("") && !setting.ip.equals(" ") && null != setting.port && !setting.port.equals(
                        ""
                    ) && !setting.port.equals(" ")
                ) {
                    view.edt_ip_address.setText(setting.ip)
                    view.edt_port_number.setText(setting.port)
                    if (setting.ssl) {

                        view.cb_ssl_enable.isChecked = true

                    } else {
                        view.cb_ssl_enable.isChecked = false
                    }
                }
            } else {
                view.edt_ip_address.setText(getString(R.string.live_ip_address))
                view.edt_port_number.setText(getString(R.string.live_port_number))
                view.cb_ssl_enable.isChecked = true
            }
        }



        view.img_close.setOnClickListener(View.OnClickListener {
            dismiss()
        })

        view.done.setOnClickListener(View.OnClickListener {
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

        fun newInstance(): DisplaySettingsDialogFragment {
            return DisplaySettingsDialogFragment()
        }
    }


}