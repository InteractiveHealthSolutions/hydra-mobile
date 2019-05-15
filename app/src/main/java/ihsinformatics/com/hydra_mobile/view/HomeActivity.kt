package ihsinformatics.com.hydra_mobile.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import ihsinformatics.com.hydra_mobile.R
import kotlinx.android.synthetic.main.activity_main.*


class HomeActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
     startActivity(Intent(this,LoginActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        img_back.setOnClickListener(this)
    }


}
