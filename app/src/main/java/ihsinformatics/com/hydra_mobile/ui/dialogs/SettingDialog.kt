package ihsinformatics.com.hydra_mobile.ui.dialogs

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import ihsinformatics.com.hydra_mobile.R

class SettingDialog : DialogFragment(), View.OnClickListener {

    private var callback: Callback? = null

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_setting_dialog, container, false)
        val close = view.findViewById<ImageButton>(R.id.fullscreen_dialog_close)
        val action = view.findViewById<TextView>(R.id.fullscreen_dialog_action)

        close.setOnClickListener(this)
        action.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View) {
        val id = v.id

        when (id) {

            R.id.fullscreen_dialog_close -> dismiss()

            R.id.fullscreen_dialog_action -> {
                callback!!.onActionClick("Whatever")
                dismiss()
            }
        }

    }

    interface Callback {

        fun onActionClick(name: String)

    }

    companion object {

        fun newInstance(): SettingDialog {
            return SettingDialog()
        }
    }

}