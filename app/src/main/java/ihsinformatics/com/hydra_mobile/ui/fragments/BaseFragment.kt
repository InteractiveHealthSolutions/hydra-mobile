package ihsinformatics.com.hydra_mobile.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import ihsinformatics.com.hydra_mobile.ui.activity.HomeActivity

import timber.log.Timber

abstract class BaseFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun isInternetConnected(): Boolean {
        var isInternetConnected = false
        try {
            isInternetConnected = (activity!!.getApplicationContext() as HomeActivity).isInternetConnected()
        } catch (e: Exception) {
            Timber.e("Error occurred while isInternetConnected(), Error = $e")
        }

        return isInternetConnected
    }

}