package ihsinformatics.com.hydra_mobile.utils

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import androidx.appcompat.app.AlertDialog
import ihsinformatics.com.hydra_mobile.R

class AlertUtil {

    companion object {
        /**
         * This Method returns a AlertDialog Builder according to the Device's Android Version
         *
         * @param context Context Object used for displaying alert and finishing Activity when Yes Pressed
         */
        fun getAlertBuilder(context: Context): AlertDialog.Builder {

            var builder: AlertDialog.Builder? = null

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                builder = AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                builder = AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
            }

            return builder!!
        }

        /**
         * This Method returns a Basic AlertDialog Builder according to the Device's Android Version
         *
         * @param context Context Object used for displaying alert and finishing Activity when Yes Pressed
         * @param title String title to be displayed on Alert Dialog
         * @param message String message to be displayed on Alert Dialog
         * @param buttonTitle String message to be displayed on Alert Dialog Button
         */
        fun getBasicDialog(
            context: Context,
            title: String,
            message: String,
            buttonTitle: String
        ): AlertDialog.Builder? {

            var builder: AlertDialog.Builder? = null

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                builder = AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                    .setTitle(title)
                    .setMessage(message)
                    .setNegativeButton(buttonTitle, DialogInterface.OnClickListener { dialogInterface, i -> })
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                builder = AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                    .setTitle(title)
                    .setMessage(message)
                    .setNegativeButton(buttonTitle, DialogInterface.OnClickListener { dialogInterface, i -> })
            }

            return builder
        }

        /**
         * This Method returns a Basic AlertDialog Builder according to the Device's Android Version
         *
         * @param context Context Object used for displaying alert and finishing Activity when Yes Pressed
         * @param title String title to be displayed on Alert Dialog
         * @param message String message to be displayed on Alert Dialog
         * @param buttonTitle String message to be displayed on Alert Dialog Button
         */
        fun getBasicDialog(
            context: Context,
            title: String,
            message: String,
            buttonTitle: String,
            buttonClickListener: DialogInterface.OnClickListener
        ): AlertDialog.Builder? {

            var builder: AlertDialog.Builder? = null

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                builder = AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                    .setTitle(title)
                    .setMessage(message)
                    .setNegativeButton(buttonTitle, buttonClickListener)
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                builder = AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                    .setTitle(title)
                    .setMessage(message)
                    .setNegativeButton(buttonTitle, buttonClickListener)
            }

            return builder
        }

        /**
         * This Method returns a ProgressDialog according to the Device's Android Version
         *
         * @param context Context Object used for displaying alert and finishing Activity when Yes Pressed
         */
        fun getProgressDialog(context: Context): ProgressDialog {
            var pd: ProgressDialog? = null

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                pd = ProgressDialog(context, R.style.ProgressDialogKitkat)
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                pd = ProgressDialog(context, R.style.ProgressDialogPostKitKat)
            }

            return pd!!
        }
    }
}