package ihsinformatics.com.hydra_mobile.ui.dialogs

import android.app.AlertDialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import ihsinformatics.com.hydra_mobile.data.repository.AppSettingRepository
import ihsinformatics.com.hydra_mobile.ui.viewmodel.AppSettingViewModel
import ihsinformatics.com.hydra_mobile.ui.viewmodel.UserViewModel
import ihsinformatics.com.hydra_mobile.utils.GlobalPreferences
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import kotlinx.android.synthetic.main.app_setting.view.*
import kotlinx.coroutines.runBlocking

class SettingDialogFragment : DialogFragment() {

    private lateinit var appSettingViewModel: AppSettingViewModel
    private lateinit var repository: AppSettingRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.app_setting, container, false)
        dialog!!.window.attributes.windowAnimations = R.style.FullScreenDialogStyle

        var repository = context?.let { AppSettingRepository(it) }
        runBlocking {
            var list = repository!!.getSettingList()
            if (list.isNotEmpty()) {
                val setting: AppSetting = list.get(0)

                if (null != setting.ip && !setting.ip.equals("") && !setting.ip.equals(" ") && null != setting.port && !setting.port.equals("") && !setting.port.equals(" ")) {
                    view.edt_ip_address.setText(setting.ip)
                    view.edt_port_number.setText(setting.port)
                    if (setting.ssl) {

                        view.cb_ssl_enable.isChecked = true

                    } else {
                        view.cb_ssl_enable.isChecked = false
                    }
                }
            }else
            {
                view.edt_ip_address.setText(getString(R.string.live_ip_address))
                view.edt_port_number.setText(getString(R.string.live_port_number))
                view.cb_ssl_enable.isChecked = true
            }
        }



        view.img_close.setOnClickListener(View.OnClickListener {
            dismiss()
        })

        view.btn_save.setOnClickListener(View.OnClickListener {

            val ipAddress = view.edt_ip_address.text.toString()
            val portNumber = view.edt_port_number.text.toString()


            if (ipAddress != null && ipAddress != "" && ipAddress != " " && portNumber != null && portNumber != "" && portNumber != " ") {

                var baseUrl = ""
                var ssl = false

                var openMRSEndpoint = context!!.getString(R.string.openmrs_endpoint)
                var test_server_ip = context!!.getString(R.string.live_ip_address)
                var test_server_port = context!!.getString(R.string.live_port_number)
                var withoutSSL = context!!.getString(R.string.without_ssl)
                var withSSL = context!!.getString(R.string.with_ssl)


                if (view.cb_ssl_enable.isChecked) {
                    baseUrl = withSSL + ipAddress + ":" + portNumber + openMRSEndpoint
                    ssl = true
                } else {
                    baseUrl = withoutSSL + ipAddress + ":" + portNumber + openMRSEndpoint
                }

                if (URLUtil.isValidUrl(baseUrl) && Patterns.WEB_URL.matcher(baseUrl).matches()) {

                    val dialog = AlertDialog.Builder(context).setMessage("By changing URL you will delete all saved data?")
                        .setTitle("Are you sure?").setNegativeButton("No") {dialog, which ->dismiss()}
                        .setPositiveButton("Yes") { dialog, which ->

                            val userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
                            userViewModel.deleteAllUsers()
                            DataAccess.getInstance().clearAllOfflineData(context)
                            saveAppSetting(ipAddress, portNumber, ssl)
                            dismiss()
                        }
                    dialog.show()




                } else {
                    ToastyWidget.getInstance().displayError(context, "Invalid ip address or port", Toast.LENGTH_SHORT)
                }
            } else {
                ToastyWidget.getInstance().displayError(context, "Please enter required fields", Toast.LENGTH_SHORT)
            }
        })

        view.btn_reset.setOnClickListener(View.OnClickListener {

            //Todo Need to change here before release or update of app on playstore by changing it to live server ip address and port  ~Taha
            view.edt_ip_address.setText(getString(R.string.live_ip_address))
            view.edt_port_number.setText(getString(R.string.live_port_number))
            view.cb_ssl_enable.isChecked = true

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

    fun saveAppSetting(ipAddress: String, portNumber: String, ssl: Boolean) {
        appSettingViewModel = ViewModelProviders.of(this).get(AppSettingViewModel::class.java)
        appSettingViewModel.deleteAll()
        appSettingViewModel.insert(AppSetting(ip = ipAddress, port = portNumber, ssl = ssl))
        Toast.makeText(activity, "setting changed", Toast.LENGTH_SHORT).show()
    }

    fun getAppSetting(): List<AppSetting> {
        appSettingViewModel = ViewModelProviders.of(this).get(AppSettingViewModel::class.java)
        return appSettingViewModel.getSettings()
    }

}