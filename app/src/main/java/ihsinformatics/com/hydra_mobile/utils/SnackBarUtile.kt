package ihsinformatics.com.hydra_mobile.utils

import android.app.Activity
import android.content.Context
import android.view.View
import ihsinformatics.com.hydra_mobile.R
import lucifer.org.snackbartest.Icon
import lucifer.org.snackbartest.MySnack

class SnackBarUtil {

    companion object {

        fun getSnackBar(activity: Activity, msg: String, bgColor: String): MySnack.SnackBuilder {
            return MySnack.SnackBuilder(activity)
                .setText(msg)
                .setTextColor("#ffffff")
                .setTextSize(20f)
                .setBgColor(bgColor)
                .setDurationInSeconds(5)  //will display for 10 seconds
                .setActionBtnColor("#f44336")
                .setIcon(Icon.WARNING)
        }
    }
}