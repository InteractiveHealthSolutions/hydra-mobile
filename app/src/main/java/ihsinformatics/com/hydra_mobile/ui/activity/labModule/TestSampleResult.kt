package ihsinformatics.com.hydra_mobile.ui.activity.labModule

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.LinearLayout
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.config.QuestionConfiguration
import com.ihsinformatics.dynamicformsgenerator.screens.BaseActivity
import com.ihsinformatics.dynamicformsgenerator.utils.Validation
import com.ihsinformatics.dynamicformsgenerator.views.widgets.InputWidget
import com.ihsinformatics.dynamicformsgenerator.views.widgets.utils.InputWidgetBakery
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.AttributesApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.Concept
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestAttribute
import ihsinformatics.com.hydra_mobile.data.remote.service.CommonLabApiService
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class TestSampleResult : BaseActivity() {

    lateinit var sessionManager: SessionManager
    lateinit var labTestAttribute: ArrayList<LabTestAttribute>
    lateinit var llMain: LinearLayout
    lateinit var labTestAttributesGetter:CommonLabApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_sample_result)
        questions = ArrayList<Question>()
        options =ArrayList<Option>()

        llMain = findViewById<LinearLayout>(R.id.formsLayout)

        sessionManager = SessionManager(this)

        val intent = intent
        val testOrderID = intent.getStringExtra("testOrderID")


        labTestAttributesGetter=RequestManager(
            applicationContext, sessionManager.getUsername(),
            sessionManager.getPassword()
        ).getPatientRetrofit().create(CommonLabApiService::class.java)


        labTestAttributesGetter.getLabTestAttribute(
            "77",
            Constant.REPRESENTATION
        ).enqueue(object : Callback<AttributesApiResponse> {
            override fun onResponse(
                call: Call<AttributesApiResponse>,
                response: Response<AttributesApiResponse>
            ) {
                Timber.e(response.message())
                if (response.isSuccessful) {
                    labTestAttribute = response.body()!!.attributes

                } else {
                    labTestAttribute = ArrayList<LabTestAttribute>()
                }
                init()
            }

            override fun onFailure(call: Call<AttributesApiResponse>, t: Throwable) {
                Timber.e(t.localizedMessage)

            }

        })

    }


    fun init() {

        var startId=1
        for(i in 0 until labTestAttribute.size){

            if(labTestAttribute.get(i).attributeType.datatypeClassname.equals("org.openmrs.customdatatype.datatype.FreeTextDatatype")){
                questions.add(Question(true, 1, startId, "", InputWidget.InputWidgetsType.WIDGET_TYPE_EDITTEXT, View.VISIBLE, Validation.CHECK_FOR_EMPTY, labTestAttribute.get(i).attributeType.display, generateUUID(), QuestionConfiguration(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES, 5, -1, " 0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", 1)))
            }
            else if(labTestAttribute.get(i).attributeType.datatypeClassname.equals("org.openmrs.customdatatype.datatype.ConceptDatatype")){
                questions.add(Question(true, 1, startId, "", InputWidget.InputWidgetsType.WIDGET_TYPE_SPINNER, View.VISIBLE, Validation.CHECK_FOR_EMPTY, labTestAttribute.get(i).attributeType.display, generateUUID(),null))


//                for(j in 0 until )
//                labTestAttributesGetter.getConcepts(
//                    labTestAttribute.get(i).attributeType.labTestType.referenceConcept.uuid
//                ).enqueue(object : Callback<Concept> {
//                    override fun onResponse(
//                        call: Call<Concept>,
//                        response: Response<Concept>
//                    ) {
//                        Timber.e(response.message())
//                        if (response.isSuccessful) {
//
//
//                        } else {
//
//                        }
//                        init()
//                    }
//
//                    override fun onFailure(call: Call<Concept>, t: Throwable) {
//                        Timber.e(t.localizedMessage)
//
//                    }
//
//                })


               // options.add()
            }
            startId++
        }

        display()
    }


    fun display() {

        val inputWidgetBakery = InputWidgetBakery()
        for (q in questions!!) {
            try {
                val optionsList: List<Option> = getOptionsByQuestionsID(q.questionId)
                q.options = optionsList

                val w = inputWidgetBakery.bakeInputWidget(this, q)
                llMain.addView(w)

                inputWidgets.put(w.questionId, w)
            } catch (je: Exception) {
                je.printStackTrace()
            }
        }

    }


    fun generateUUID(): String? {
        val uuid = UUID.randomUUID()
        return uuid.toString()
    }

    fun getOptionsByQuestionsID(questionId: Int): List<Option> {
        val localIterator =
            Collections.enumeration(options)
        val op: MutableList<Option> =
            java.util.ArrayList()
        while (true) {
            if (!localIterator.hasMoreElements()) {
                return op
            }
            val localOption =
                localIterator.nextElement() as Option
            if (localOption.uuid != null) {
                if (localOption.questionId == questionId) op.add(localOption)
            }
        }
    }

}
