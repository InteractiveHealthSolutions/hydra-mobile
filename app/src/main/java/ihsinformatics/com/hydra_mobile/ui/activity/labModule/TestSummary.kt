package ihsinformatics.com.hydra_mobile.ui.activity.labModule

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestOrder
import org.jetbrains.anko.textAppearance


class TestSummary : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M) override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_summary)

        val intent = intent
        val jsonObj = intent.getStringExtra("lab")
        val jsonAttributes = intent.getStringExtra("attributes")


        if (jsonObj != null && !jsonObj.equals("")) {
            val gson = Gson()
            val token: TypeToken<LabTestOrder?> = object : TypeToken<LabTestOrder?>() {}
            val testOrder: LabTestOrder = gson.fromJson(jsonObj, token.type)


            //Layout 2
            findViewById<TextView>(R.id.testOrderID).setText(testOrder.testOrderId)
            findViewById<TextView>(R.id.testGroup).setText(testOrder.labTestType.testGroup)
            findViewById<TextView>(R.id.testType).setText(testOrder.labTestType.name)
            findViewById<TextView>(R.id.encounterType).setText(testOrder.order.encounter.display)
            findViewById<TextView>(R.id.labReferenceNumber).setText(testOrder.labReferenceNumber)
            findViewById<TextView>(R.id.requireSpecimen).setText(testOrder.labTestType.requiresSpecimen.toString())
            findViewById<TextView>(R.id.createdBy).setText(testOrder.auditInfo.creator.display)
            findViewById<TextView>(R.id.dateCreated).setText(testOrder.auditInfo.dateCreated)
            findViewById<TextView>(R.id.uuid).setText(testOrder.uuid)

            var ll_layout = findViewById<LinearLayout>(R.id.labTestSample)

            val lparams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 4.0f)


            val lparamsWithMargin = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 4.0f)
            lparamsWithMargin.setMargins(0, 0, 0, 20)

            for (i in 0 until testOrder.labTestSamples.size) {
                val firstTextView = TextView(this)
                firstTextView.setText("Test Order: ")
                firstTextView.textAppearance=R.style.fontForSummaryHeading


                val secondTextView = TextView(this)
                secondTextView.setText(testOrder.labTestSamples.get(i).display.toString())
                secondTextView.textAppearance=R.style.fontForSummaryValue

                var singleLabSample = LinearLayout(this)
                singleLabSample.layoutParams = lparamsWithMargin
                singleLabSample.addView(secondTextView, LinearLayout.HORIZONTAL)
                singleLabSample.addView(firstTextView, LinearLayout.HORIZONTAL)
                ll_layout.addView(singleLabSample)


                val firstTextView2 = TextView(this)
                firstTextView2.setText("Specimen Type: ")
                firstTextView2.textAppearance=R.style.fontForSummaryHeading

                val secondTextView2 = TextView(this)
                secondTextView2.setText(testOrder.labTestSamples.get(i).specimenType.display)
                secondTextView2.textAppearance=R.style.fontForSummaryValue

                var singleLabSample2 = LinearLayout(this)
                singleLabSample2.layoutParams = lparamsWithMargin
                singleLabSample2.addView(secondTextView2, LinearLayout.HORIZONTAL)
                singleLabSample2.addView(firstTextView2, LinearLayout.HORIZONTAL)
                ll_layout.addView(singleLabSample2)


                val firstTextView3 = TextView(this)
                firstTextView3.setText("Specimen Site: ")
                 firstTextView3.textAppearance=R.style.fontForSummaryHeading

                val secondTextView3 = TextView(this)
                secondTextView3.setText(testOrder.labTestSamples.get(i).specimenSite.display)
                secondTextView3.textAppearance=R.style.fontForSummaryValue

                var singleLabSample3 = LinearLayout(this)
                singleLabSample3.layoutParams = lparamsWithMargin
                singleLabSample3.addView(secondTextView3, LinearLayout.HORIZONTAL)
                singleLabSample3.addView(firstTextView3, LinearLayout.HORIZONTAL)
                ll_layout.addView(singleLabSample3)

                val firstTextView4 = TextView(this)
                firstTextView4.setText("Status: ")
               firstTextView4.textAppearance=R.style.fontForSummaryHeading

                val secondTextView4 = TextView(this)
                secondTextView4.setText(testOrder.labTestSamples.get(i).status)
                secondTextView4.textAppearance=R.style.fontForSummaryValue

                var singleLabSample4 = LinearLayout(this)
                singleLabSample4.layoutParams = lparamsWithMargin
                singleLabSample4.addView(secondTextView4, LinearLayout.HORIZONTAL)
                singleLabSample4.addView(firstTextView4, LinearLayout.HORIZONTAL)
                ll_layout.addView(singleLabSample4)

            }

        }

        //Layout 1

        findViewById<TextView>(R.id.groupName).setText("RADIOLOGY")
        findViewById<TextView>(R.id.question).setText("No Questions")
        findViewById<TextView>(R.id.value).setText("No value")


    }

    override fun onBackPressed() {

        startActivity(Intent(this, CommonLabActivity::class.java))
        finish()
        super.onBackPressed()
    }


}
