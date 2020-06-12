package ihsinformatics.com.hydra_mobile.ui.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Path
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import kotlinx.android.synthetic.main.activity_splash.*
import yanzhikai.textpath.painter.AsyncPathPainter

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // initAnimation();
        var imgLogo: ImageView = findViewById(R.id.ivLogo)
        flipit(imgLogo)
    }

    private fun flipit(viewToFlip: View) {
//        val flip = ObjectAnimator.ofFloat(viewToFlip, "rotationY", 0f, 360f)
//        flip.duration = 3500
//        flip.start()

        val secondsDelayed = 1
        Handler().postDelayed({
            if (SessionManager(applicationContext).isLoggedIn()) {
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }

        }, (secondsDelayed * 4000).toLong())

    }
    /*private fun initAnimation() {
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
            if (SessionManager(applicationContext).isLoggedIn()) {
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }

        }, (secondsDelayed * 4500).toLong())
    }*/
}
