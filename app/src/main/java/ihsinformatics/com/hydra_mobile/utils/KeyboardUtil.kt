package ihsinformatics.com.hydra_mobile.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import timber.log.Timber

/**
 * Created by shujaat.ali on 6/24/2019.
 */


class KeyboardUtil {

    companion object {

        fun hideSoftKeyboard(activity: Activity) {
            try {
                val inputMethodManager = activity.getSystemService(
                    Activity.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    activity.currentFocus!!.windowToken, 0
                )
            } catch (e: Exception) {
                Timber.e("Error occurred!, Error = $e")
            }
        }

    }
}