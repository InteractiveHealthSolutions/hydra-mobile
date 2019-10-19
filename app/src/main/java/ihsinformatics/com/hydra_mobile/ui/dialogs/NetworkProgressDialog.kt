package ihsinformatics.com.hydra_mobile.ui.dialogs

import android.app.ProgressDialog
import android.content.Context

@Suppress("DEPRECATION")
class NetworkProgressDialog : ProgressDialog {

    constructor(context: Context) : super(context) {
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        setMessage("Please wait...")
        setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER)
    }


    fun show(message: String) {
        setMessage(message)
        super.show()
    }
}