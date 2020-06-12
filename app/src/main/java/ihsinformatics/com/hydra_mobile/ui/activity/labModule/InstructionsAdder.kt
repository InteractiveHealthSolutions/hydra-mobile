package ihsinformatics.com.hydra_mobile.ui.activity.labModule

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import ihsinformatics.com.hydra_mobile.R

class InstructionsAdder : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instructions_adder)

        val dm= DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        var width = dm.widthPixels
        var height = dm.heightPixels

        window.setLayout((width *0.8).toInt(),(height*0.7).toInt())

        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }
}
