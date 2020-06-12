package ihsinformatics.com.hydra_mobile.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.databinding.ActivityPatientContactBinding


class PatientContact : AppCompatActivity() {

    lateinit var binding: ActivityPatientContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_patient_contact)

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        var width = dm.widthPixels
        var height = dm.heightPixels

        window.setLayout((width * 0.8).toInt(), (height * 0.4).toInt())

        // window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        //this.setFinishOnTouchOutside(false);

        val scale = resources.displayMetrics.density
        //val dpAsPixels = (20 * scale + 0.5f).toInt()


        binding.callLayout.setOnClickListener {

            try {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+923352155194"))
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                ToastyWidget.getInstance().displayError(this, "Error Calling", Toast.LENGTH_SHORT)
            }
        }


        binding.whatappLayout.setOnClickListener {

            try {
                val uri = Uri.parse("https://wa.me/923352155194");
                val intent = Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                finish()
            } catch (e: Exception) {
                ToastyWidget.getInstance().displayError(this, "Error", Toast.LENGTH_SHORT)
            }

        }

        binding.clipboardLayout.setOnClickListener {

            try {
                val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("number", "+923352155194")
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this,"Number copied to clipboard",Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {
                ToastyWidget.getInstance().displayError(this, "Error", Toast.LENGTH_SHORT)
            }

        }

        binding.close.setOnClickListener{
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


