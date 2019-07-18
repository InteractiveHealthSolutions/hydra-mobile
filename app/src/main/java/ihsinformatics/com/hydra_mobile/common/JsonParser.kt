package ihsinformatics.com.hydra_mobile.common

import android.content.Context
import org.json.JSONObject
import java.io.IOException

class JsonParser {

    fun readJsonFile(context: Context): JSONObject {

        return try {

            val inputStream = context.assets.open("workflow.json")
                .bufferedReader().use { it.readText() }
            JSONObject(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            JSONObject("{}")
        }
    }
}