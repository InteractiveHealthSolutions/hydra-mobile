package ihsinformatics.com.hydra_mobile.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import ihsinformatics.com.hydra_mobile.R
import androidx.databinding.DataBindingUtil
import android.view.MotionEvent
import ihsinformatics.com.hydra_mobile.databinding.ActivityTermsAndConditionsBinding


class TermsAndConditions : AppCompatActivity() {

    lateinit var binding: ActivityTermsAndConditionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_terms_and_conditions)

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        var width = dm.widthPixels
        var height = dm.heightPixels

        window.setLayout((width * 0.8).toInt(), (height * 0.6).toInt())

        // window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        //this.setFinishOnTouchOutside(false);

        val scale = resources.displayMetrics.density
        val dpAsPixels = (20 * scale + 0.5f).toInt()


        binding.acceptButton.setOnClickListener {
            if (binding.acceptConditon.isChecked) {
                val returnIntent = Intent()
                returnIntent.putExtra("result", true)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        }


        binding.cancelButton.setOnClickListener {
            val returnIntent = Intent()
            returnIntent.putExtra("result", false)
            setResult(Activity.RESULT_CANCELED, returnIntent)
            finish()
        }

    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        // If we've received a touch notification that the user has touched
        // outside the app, finish the activity.
        if (MotionEvent.ACTION_OUTSIDE == event.action) {
            finish()
            return true
        }

        // Delegate everything else to Activity.
        return super.onTouchEvent(event)
    }


}


