package ihsinformatics.com.hydra_mobile.ui.activity.labModule

//TODO error while parsing edittext answer and api not accepting result ~Taha CommonLAB
//TODO also cannot display summary results since attribute field is not coming   ~Taha CommonLAB

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
import com.ihsinformatics.dynamicformsgenerator.utils.Validation
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidget
import com.ihsinformatics.dynamicformsgenerator.views.widgets.utils.InputWidgetBakery
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.AttributeTypeResponse
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.AttributesApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.CommonLabApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.*
import ihsinformatics.com.hydra_mobile.data.remote.service.CommonLabApiService
import ihsinformatics.com.hydra_mobile.ui.dialogs.NetworkProgressDialog
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import kotlin.collections.ArrayList

class TestSampleResult : BaseActivity() {

    lateinit var sessionManager: SessionManager
    lateinit var labTestAttribute: ArrayList<AttributeTypeResponse>
    lateinit var llMain: LinearLayout
    lateinit var labTestAttributesGetter: CommonLabApiService

    lateinit var sendParams: JSONObject
    lateinit var attributesObj: JSONObject
    lateinit var attributes: JSONArray
    private lateinit var networkProgressDialog: NetworkProgressDialog

    var myQuestions = ArrayList<Question>()
    var inputWidgetMap= HashMap<String, InputWidget>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_sample_result)

        networkProgressDialog = NetworkProgressDialog(this)
        networkProgressDialog.show()

        questions = ArrayList<Question>()
        options = ArrayList<Option>()


        llMain = findViewById<LinearLayout>(R.id.formsLayout)
        var saveResults = findViewById<Button>(R.id.saveResults)


        sessionManager = SessionManager(this)

        val intent = intent
        val testTypeUuid = intent.getStringExtra("testTypeUuid")
        val orderUUID = intent.getStringExtra("OrderUUID")
        val labReference = intent.getStringExtra("labReferenceNumber")
        val labTestTypeUUID = intent.getStringExtra("labTestType")

        labTestAttributesGetter = RequestManager(applicationContext, sessionManager.getUsername(), sessionManager.getPassword()).getPatientRetrofit().create(CommonLabApiService::class.java)



        labTestAttributesGetter.getLabTestAttributeType(testTypeUuid, Constant.REPRESENTATION).enqueue(object : Callback<AttributesApiResponse> {
            override fun onResponse(
                call: Call<AttributesApiResponse>, response: Response<AttributesApiResponse>
                                   ) {
                Timber.e(response.message())
                if (response.isSuccessful) {
                    labTestAttribute = response.body()!!.attributes
                    init()

                } else {
                    networkProgressDialog.dismiss()
                    ToastyWidget.getInstance().displayError(this@TestSampleResult, "Error fetching Test Results Meta Data", Toast.LENGTH_LONG)
                    startActivity(Intent(this@TestSampleResult, CommonLabActivity::class.java))
                }


            }

            override fun onFailure(call: Call<AttributesApiResponse>, t: Throwable) {
                Timber.e(t.localizedMessage)
                networkProgressDialog.dismiss()
                ToastyWidget.getInstance().displayError(this@TestSampleResult, getString(R.string.internet_issue), Toast.LENGTH_LONG)
                startActivity(Intent(this@TestSampleResult, CommonLabActivity::class.java))
            }

        })

        saveResults.setOnClickListener {

            networkProgressDialog.show()

            attributesObj = JSONObject()
            sendParams = JSONObject()
            attributes = JSONArray()
            val inputWidgetBakery = InputWidgetBakery()

            for (i in myQuestions) {
               // var w = inputWidgetBakery.bakeInputWidget(this, i)
                var w=inputWidgetMap.get(i.paramName)
                attributesObj = JSONObject()
                attributesObj.put("attributeType", i.paramName)
                attributesObj.put("valueReference", w!!.value)
                attributes.put(attributesObj)
            }


            sendParams.put("order", orderUUID)
            sendParams.put("labReferenceNumber", labReference)
            sendParams.put("labTestType", labTestTypeUUID)
            sendParams.put("attributes", attributes)


            val body: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), sendParams.toString())
            labTestAttributesGetter.addLabTestOrder(body).enqueue(object : Callback<CommonLabApiResponse> {
                override fun onResponse(
                    call: Call<CommonLabApiResponse>, response: Response<CommonLabApiResponse>
                                       ) {
                    Timber.e(response.message())
                    if (response.isSuccessful) {

                        networkProgressDialog.dismiss()
                        ToastyWidget.getInstance().displaySuccess(this@TestSampleResult, "Result saved successfully", Toast.LENGTH_SHORT)

                        startActivity(Intent(this@TestSampleResult, CommonLabActivity::class.java))
                    } else {
                        networkProgressDialog.dismiss()
                        ToastyWidget.getInstance().displayError(this@TestSampleResult, "Error saving Result", Toast.LENGTH_SHORT)

                    }
                }

                override fun onFailure(call: Call<CommonLabApiResponse>, t: Throwable) {
                    Timber.e(t.localizedMessage)
                    networkProgressDialog.dismiss()
                    ToastyWidget.getInstance().displayError(this@TestSampleResult, getString(R.string.internet_issue), Toast.LENGTH_LONG)
                    startActivity(Intent(this@TestSampleResult, CommonLabActivity::class.java))
                }

            })

        }


    }


    fun init() {

        var questionsId = 1
        var optionsID = 1
        for (i in 0 until labTestAttribute.size) {

            if (labTestAttribute.get(i).datatypeClassname.equals("org.openmrs.customdatatype.datatype.FreeTextDatatype")) {
                var q = Question(true, 1, questionsId, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, labTestAttribute.get(i).display, labTestAttribute.get(i).uuid, QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 5, -1, " 0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1))


                display(q, ArrayList<Option>())

            } else if (labTestAttribute.get(i).datatypeClassname.equals("org.openmrs.customdatatype.datatype.ConceptDatatype")) {

                var q = Question(true, 1, questionsId, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, labTestAttribute.get(i).display, labTestAttribute.get(i).uuid, null)



                labTestAttributesGetter.getConcepts(labTestAttribute.get(i).datatypeConfig).enqueue(object : Callback<Concept> {
                    override fun onResponse(
                        call: Call<Concept>, response: Response<Concept>
                                           ) {
                        Timber.e(response.message())
                        if (response.isSuccessful && null != response.body()) {
                            var o = ArrayList<Option>()
                            for (i in 0 until response.body()!!.answers.size) {
                                o.add(Option(questionsId, optionsID, null, null, response.body()!!.answers[i].uuid, response.body()!!.answers[i].display, -1))
                                optionsID++
                            }

                            display(q, o)
                        }
                    }

                    override fun onFailure(call: Call<Concept>, t: Throwable) {
                        Timber.e(t.localizedMessage)

                        ToastyWidget.getInstance().displayError(this@TestSampleResult, getString(R.string.internet_issue), Toast.LENGTH_SHORT)
                        startActivity(Intent(this@TestSampleResult, CommonLabActivity::class.java))
                    }

                })


            } else   //TODO There are alot more datatypes as well such as float etc... you need to figure out all datatypes and according make widget rather making all widgets edit text   ~Taha
            {
                var q = Question(true, 1, questionsId, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, labTestAttribute.get(i).display, labTestAttribute.get(i).uuid, QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 5, -1, " 0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1))


                display(q, ArrayList<Option>())
            }
            questionsId++
        }

        networkProgressDialog.dismiss()

    }   //TODO Questions that are manadatory are getting saved if they are not filled


    fun display(question: Question, optionsList: List<Option>) {

        val inputWidgetBakery = InputWidgetBakery()
        try {
            question.options = optionsList
            myQuestions.add(question)
            val w= inputWidgetBakery.bakeInputWidget(this, question)
            inputWidgetMap.put(question.paramName,w)
            llMain.addView(w)

            inputWidgets.put(w.questionId, w)
        } catch (je: Exception) {
            je.printStackTrace()
        }

    }


}

