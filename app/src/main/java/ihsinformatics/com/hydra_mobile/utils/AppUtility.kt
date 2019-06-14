package ihsinformatics.com.hydra_mobile.utils

import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.util.TypedValue
import androidx.appcompat.app.AlertDialog
import java.util.*
import java.util.concurrent.TimeUnit

/**
 *
 *
 */

class AppUtility {

    companion object {
        /**
         * Converting dp to pixel
         */
        fun dpToPx(r: Resources, dp: Int): Int {
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
        }

        fun getDifferenceDays(d1: Date, d2: Date): Long {
            val diff = d2.time - d1.time
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
        }

        fun showMessageOKCancel(context: Context, message: String, okListener: DialogInterface.OnClickListener) {
            AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }
    }

}