package ihsinformatics.com.hydra_mobile.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.DialogFragment
import com.victor.loading.newton.NewtonCradleLoading
import ihsinformatics.com.hydra_mobile.R

class LoadingProgressDialog : DialogFragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.loading_screen, container, false)
        dialog!!.window.attributes.windowAnimations = R.style.LoadingScreeningThem

        val newtonCradleLoading = view.findViewById<NewtonCradleLoading>(R.id.newton_cradle_loading)
        newtonCradleLoading.start()
        newtonCradleLoading.setLoadingColor(R.color.colorPrimary)

        return view;
    }

    companion object {
        fun newInstance(): LoadingProgressDialog {
            return LoadingProgressDialog()
        }
    }


}