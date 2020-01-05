package ihsinformatics.com.hydra_mobile

import android.app.Application
import android.content.Context
import android.widget.Toast
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.*
import ihsinformatics.com.hydra_mobile.data.repository.*
import org.json.JSONArray
import java.io.IOException
import org.json.JSONException

/**
 * File Description
 * <p>
 * Author: shujaat ali
 * Email: shujaat.ali@ihsinformatics.com
 */


class HydraApp : Application() {
    
    companion object {
        var context: Context? = null;
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext;
    }

}