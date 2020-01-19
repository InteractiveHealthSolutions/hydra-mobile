package ihsinformatics.com.hydra_mobile.ui.activity.labModule

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestOrder


class TestSummary : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_summary)

        val intent = intent
        val jsonObj = intent.getStringExtra("lab")

        val gson = Gson()
        val token: TypeToken<LabTestOrder?> =
            object : TypeToken<LabTestOrder?>() {}
        val testOrder: LabTestOrder = gson.fromJson(jsonObj, token.type)


        //Layout 1
        findViewById<TextView>(R.id.groupName).setText(testOrder.labTestType.testGroup)
        findViewById<TextView>(R.id.question).setText("This is fake text")
        findViewById<TextView>(R.id.value).setText("This is fake text")

        //Layout 2
        findViewById<TextView>(R.id.testOrderID).setText("This is fake text")
        findViewById<TextView>(R.id.testGroup).setText(testOrder.labTestType.testGroup)
        findViewById<TextView>(R.id.testType).setText("This is fake text")
        findViewById<TextView>(R.id.encounterType).setText("This is fake text")
        findViewById<TextView>(R.id.labReferenceNumber).setText(testOrder.labReferenceNumber)
        findViewById<TextView>(R.id.requireSpecimen).setText("This is fake text")
        findViewById<TextView>(R.id.createdBy).setText("This is fake text")
        findViewById<TextView>(R.id.dateCreated).setText("This is fake text")
        findViewById<TextView>(R.id.uuid).setText("This is fake text")
        findViewById<TextView>(R.id.testOrder).setText("This is fake text")
        findViewById<TextView>(R.id.specimenType).setText("This is fake text")
        findViewById<TextView>(R.id.specimenSite).setText("This is fake text")
        findViewById<TextView>(R.id.status).setText("This is fake text")


    }

    override fun onBackPressed() {

        startActivity(Intent(this, CommonLabActivity::class.java))
        finish()
        super.onBackPressed()
    }


}
