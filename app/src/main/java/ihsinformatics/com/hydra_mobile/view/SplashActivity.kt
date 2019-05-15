package ihsinformatics.com.hydra_mobile.view

import android.content.Intent
import android.graphics.Path
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import ihsinformatics.com.hydra_mobile.R
import kotlinx.android.synthetic.main.activity_splash.*
import yanzhikai.textpath.painter.AsyncPathPainter

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initAnimation();
    }
    fun initAnimation(){
        atpv_text.startAnimation(0f, 1f)
        atpv_text.setPathPainter(AsyncPathPainter { x, y, paintPath ->
            paintPath.addCircle(
                x,
                y,
                6f,
                Path.Direction.CW
            )
        })
        val secondsDelayed = 1
        Handler().postDelayed({
            atpv_text.stopAnimation()
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }, (secondsDelayed * 4500).toLong())
    }
}
