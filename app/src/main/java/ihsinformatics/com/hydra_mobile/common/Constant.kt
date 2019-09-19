package ihsinformatics.com.hydra_mobile.common

import ihsinformatics.com.hydra_mobile.data.local.entities.User
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.ComponentForm
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Forms
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Constant {
    companion object {

        val TIME_FORMAT = SimpleDateFormat("HH:mm:ss a", Locale.US)
        val DATE_FORMAT = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US)
        /* lateinit var DATE_FORMAT: SimpleDateFormat
         lateinit var TIME_FORMAT: SimpleDateFormat*/
        val BASE_URL = "http://mrs.ghd.ihn.org.pk/openmrs/ws/rest/v1"
        val REPRESENTATION = "full"
        val REQUEST_TIMEOUT = 60
        var USERNAME: String? = null
        var PASSWORD: String? = null
        val OPENMRS_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        val OPENMRS_TIMESTAMP_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

        var errorMessage = arrayOf(
            "No response from server",
            "Could not connect to server",
            "Invalid http protocol",
            "An error occured, Invalid response from server",
            "A patient with same MR number already exists",
            "Request not completed, error code: ",
            "Unsupported encoding scheme"
        )
        var errorMessagesList = ArrayList(Arrays.asList(*errorMessage))
        val DATE_TIME = 6
        val DATE = 7
        val TIME = 8
        val PATIENT_LOAD_VIA_TAG = 9
        val PERIOD_REOPEN = 10
        val PERIOD_CREATE = 11
        var AGE_DOB_DEADLOCK: Boolean? = false

        var SCOREABLE_QUESTIONS = Arrays.asList(*arrayOf(9, 11, /*12,*/13, 14, 56, 15, 16, 17, 18))

        val KEY_LOAD_DATA = "load_data"
        val KEY_JSON_DATA = "json_data"
        val RESPONSE_NUMBER = "responseNumber"

        //
        var formList: List<Forms> = ArrayList()
        var componentName = ""
        var formName = ""
        var tempLogin: Boolean = false
        //

        var currentUser  =  User()
    }


}