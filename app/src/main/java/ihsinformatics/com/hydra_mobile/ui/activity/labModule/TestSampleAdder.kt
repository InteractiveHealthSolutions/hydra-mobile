package ihsinformatics.com.hydra_mobile.ui.activity.labModule


import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.QuestionConfiguration
import com.ihsinformatics.dynamicformsgenerator.screens.BaseActivity
import com.ihsinformatics.dynamicformsgenerator.screens.dialogs.DateSelector
import com.ihsinformatics.dynamicformsgenerator.utils.Validation
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidget
import com.ihsinformatics.dynamicformsgenerator.views.widgets.utils.InputWidgetBakery
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.Concept
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.TestSampleApi
import ihsinformatics.com.hydra_mobile.data.remote.service.CommonLabApiService
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TestSampleAdder : BaseActivity() {


    lateinit var sessionManager: SessionManager
    lateinit var llMain: LinearLayout           //TODO DISCUSS what if these concepts are changed in OPENMRS, since we are using hardcoded concept values here ~Taha
    var specimenSiteUUID = "159959AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"   //These concepts should be hardcoded
    var specimenTypeUUID = "162476AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    var unitUUID = "5db4f53e-6218-4ae0-ae4e-5e0343b5d301"


    var specimenTypeQuestionID = 1001
    var specimenSiteQuestionID = 1002
    var quantityQuestionID = 1003
    var unitQuestionID = 1004
    var sampleIdentifierQuestionID = 1005
    var dateQuestionID = 1006

    lateinit var projectStartDate: Date
    lateinit var cal: Calendar
    lateinit var lastMonday: Date
    lateinit var lastYear: Date
    lateinit var nextYear: Date
    lateinit var today: Date
    lateinit var date110YearsAgo: Date
    lateinit var testSampleConcepts: CommonLabApiService

    lateinit var labTest: String
    val comments: String = "None"
    val status: String = "Collected"   //Initially sample status will be collected
    var expiryDate = null

    var inputWidgetMap= HashMap<String, InputWidget>()

    lateinit var questionSpecimenType: Question
    lateinit var questionSpecimenSite: Question
    lateinit var questionQuantity: Question
    lateinit var questionUnit: Question
    lateinit var questionSampleIdentitfier: Question
    lateinit var questionDateOfCollection: Question

    val inputWidgetBakery = InputWidgetBakery()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_sample_adder)

        sessionManager = SessionManager(this)

        llMain = findViewById<LinearLayout>(R.id.formsLayout)

        var saveTestResults = findViewById<Button>(R.id.saveTestResults)

        testSampleConcepts = RequestManager(applicationContext, sessionManager.getUsername(), sessionManager.getPassword()).getPatientRetrofit().create(CommonLabApiService::class.java)

        val intent = intent
        labTest = intent.getStringExtra("labTest")



        saveTestResults.setOnClickListener {

            var dateStr = inputWidgetBakery.bakeInputWidget(this, questionDateOfCollection).value.toString()

            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.ENGLISH)

            val parsedDate = sdf.parse(dateStr);
            val print = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            var collectionDate = print.format(parsedDate)

            //TODO question cannot get quantity and sample thats why one cant send it... ~Taha

            var sendParams = JSONObject()
            sendParams.put("labTest", labTest)
            sendParams.put("specimenType", getSelectedValueUUID(questionSpecimenType))
            sendParams.put("specimenSite", getSelectedValueUUID(questionSpecimenSite))
            sendParams.put("quantity", inputWidgetMap.get(questionQuantity.paramName)!!.value)
            sendParams.put("sampleIdentifier", inputWidgetMap.get(questionSampleIdentitfier.paramName)!!.value)
            sendParams.put("collectionDate", collectionDate)
            sendParams.put("collector", sessionManager.getProviderUUID())
            sendParams.put("expiryDate", expiryDate)
            sendParams.put("status", status)
            sendParams.put("comments", comments)


            val body: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), sendParams.toString())

            testSampleConcepts.addTestSample(body).enqueue(object : Callback<TestSampleApi> {
                override fun onFailure(call: Call<TestSampleApi>, t: Throwable) {

                    ToastyWidget.getInstance().displayError(this@TestSampleAdder, "Error Adding Test Sample", Toast.LENGTH_SHORT)
                    startActivity(Intent(this@TestSampleAdder, CommonLabActivity::class.java))

                }

                override fun onResponse(
                    call: Call<TestSampleApi>, response: Response<TestSampleApi>
                                       ) {
                    if (response.isSuccessful) {
                        ToastyWidget.getInstance().displaySuccess(this@TestSampleAdder, "Test Sample Added Successfully", Toast.LENGTH_SHORT)
                        startActivity(Intent(this@TestSampleAdder, CommonLabActivity::class.java))
                    } else {
                        ToastyWidget.getInstance().displayError(this@TestSampleAdder, "Error Adding Test Sample", Toast.LENGTH_SHORT)
                    }
                }

            })

        }

        initDates()

        initQuestions()

        fetchFromAPIs()
    }


    fun initQuestions() {

        questionSpecimenType = Question(true, 1, specimenTypeQuestionID, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Specimen Type", "specimenType", null)

        questionSpecimenSite = Question(true, 1, specimenSiteQuestionID, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Specimen Site", "specimenSite", null)

        questionQuantity = Question(true, 1, quantityQuestionID, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Quantity", "quantity", QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 5, -1, " 0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1))
        display(null, questionQuantity)


        questionUnit = Question(true, 1, unitQuestionID, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Unit", "unit", null)

        questionSampleIdentitfier = Question(true, 1, sampleIdentifierQuestionID, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, "Sample Identifier", "sampleIdentifier", QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 5, -1, " 0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1))

        display(null, questionSampleIdentitfier)

        var dob = QuestionConfiguration(today, projectStartDate, DateSelector.WIDGET_TYPE.DATE, 9)

        questionDateOfCollection = Question(true, 1, dateQuestionID, "", InputWidget.InputWidgetsType.WIDGET_TYPE_DATE, View.VISIBLE, Validation.CHECK_FOR_DATE, "Collection Date ", "collectionDate", dob)

        display(null, questionDateOfCollection)


    }


    private fun initDates() {
        try {
            projectStartDate = SimpleDateFormat("yyyyMMdd").parse("20160601")
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        val localCalendar = Calendar.getInstance()
        localCalendar[Calendar.DAY_OF_WEEK] = 2
        lastMonday = localCalendar.time
        cal = Calendar.getInstance()
        today = cal.time
        cal.add(Calendar.YEAR, 1)
        nextYear = cal.time
        cal.add(Calendar.YEAR, -2)
        lastYear = cal.time
        cal.add(Calendar.YEAR, -109)
        date110YearsAgo = cal.time
    }

    fun display(optionsList: List<Option>?, question: Question) {

        try {

            if (null != question) {

                if (null != optionsList) question.options = optionsList

                val w = inputWidgetBakery.bakeInputWidget(this, question)
                inputWidgetMap.put(question.paramName,w)
                llMain.addView(w)

                inputWidgets.put(w.questionId, w)
            } else {
                ToastyWidget.getInstance().displayError(this, "Form Cannot be fetched", Toast.LENGTH_SHORT)
                startActivity(Intent(this, CommonLabActivity::class.java))
            }

        } catch (je: Exception) {
            je.printStackTrace()
        }

    }


    fun fetchSpecimenTypeConceptOptions() {
        testSampleConcepts.getConcepts(specimenTypeUUID).enqueue(object : Callback<Concept> {
            override fun onResponse(
                call: Call<Concept>, response: Response<Concept>
                                   ) {
                Timber.e(response.message())
                if (response.isSuccessful && null != response.body()) {
                    var o = ArrayList<Option>()
                    for (i in 0 until response.body()!!.answers.size) {
                        o.add(Option(specimenTypeQuestionID, 11, null, null, response.body()!!.answers[i].uuid, response.body()!!.answers[i].display, -1))
                    }

                    display(o, questionSpecimenType)
                }
            }

            override fun onFailure(call: Call<Concept>, t: Throwable) {
                Timber.e(t.localizedMessage)

                ToastyWidget.getInstance().displayError(this@TestSampleAdder, getString(R.string.internet_issue), Toast.LENGTH_SHORT)
                startActivity(Intent(this@TestSampleAdder, CommonLabActivity::class.java))
            }

        })
    }


    fun fetchSpecimenSiteConceptOptions() {
        testSampleConcepts.getConcepts(specimenSiteUUID).enqueue(object : Callback<Concept> {
            override fun onResponse(
                call: Call<Concept>, response: Response<Concept>
                                   ) {
                Timber.e(response.message())
                if (response.isSuccessful && null != response.body()) {
                    var o = ArrayList<Option>()
                    for (i in 0 until response.body()!!.answers.size) {
                        o.add(Option(specimenSiteQuestionID, 11, null, null, response.body()!!.answers[i].uuid, response.body()!!.answers[i].display, -1))
                    }

                    display(o, questionSpecimenSite)
                }
            }

            override fun onFailure(call: Call<Concept>, t: Throwable) {
                Timber.e(t.localizedMessage)

                ToastyWidget.getInstance().displayError(this@TestSampleAdder, getString(R.string.internet_issue), Toast.LENGTH_SHORT)
                startActivity(Intent(this@TestSampleAdder, CommonLabActivity::class.java))
            }

        })
    }


    fun fetchUnitConceptOptions() {
        testSampleConcepts.getConcepts(unitUUID).enqueue(object : Callback<Concept> {
            override fun onResponse(
                call: Call<Concept>, response: Response<Concept>
                                   ) {
                Timber.e(response.message())
                if (response.isSuccessful && null != response.body()) {
                    var o = ArrayList<Option>()
                    for (i in 0 until response.body()!!.answers.size) {
                        o.add(Option(unitQuestionID, 11, null, null, response.body()!!.answers[i].uuid, response.body()!!.answers[i].display, -1))
                    }

                    display(o, questionUnit)
                }
            }

            override fun onFailure(call: Call<Concept>, t: Throwable) {
                Timber.e(t.localizedMessage)

                ToastyWidget.getInstance().displayError(this@TestSampleAdder, getString(R.string.internet_issue), Toast.LENGTH_SHORT)
                startActivity(Intent(this@TestSampleAdder, CommonLabActivity::class.java))
            }

        })
    }


    fun fetchFromAPIs() {
        fetchSpecimenSiteConceptOptions()
        fetchSpecimenTypeConceptOptions()
        fetchUnitConceptOptions()
    }

    fun getSelectedValueUUID(question: Question): String {
        val selectedValue = inputWidgetMap.get(question.paramName)!!.value

        for (i in question.options) {
            if (i.text.equals(selectedValue)) {
                return i.uuid
            }
        }
        return ""
    }
}
