package ihsinformatics.com.hydra_mobile.ui.activity

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import ihsinformatics.com.hydra_mobile.R
import org.jetbrains.anko.padding
import java.text.SimpleDateFormat
import java.util.*


class UserReports : AppCompatActivity() {

    private lateinit var tableLayout: TableLayout
    private lateinit var username: TextView
    private lateinit var date: TextView
    private lateinit var spWorkflow: Spinner

    private var isEven = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_reports)

        initViews()

        val userReports = DataAccess.getInstance()
            .getAllUserReportsByUserName(this, Global.USERNAME)
        for (report in userReports) {
            displayDataInTable(report.encounter, "1", report.encounter_uploaded.toString())
        }

    }


    private fun initViews() {

        tableLayout = findViewById(R.id.tableLayout)

        username = findViewById(R.id.username)
        username.setText(Global.USERNAME)

        date = findViewById(R.id.date)
        val dateValue: String = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(Date())
        date.setText(dateValue)

        spWorkflow = findViewById(R.id.workflowsSpinner)
        val languages = resources.getStringArray(R.array.countries_array)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        spWorkflow.adapter = adapter

        spWorkflow.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

    }


    private fun displayDataInTable(formName: String, filled: String, uploaded: String) {

        val formNameTV = TextView(this)
        val timesFormFilledTV = TextView(this)
        val timesFormUploadedTV = TextView(this)

        val tableRow = TableRow(this)
        tableRow.padding = 5
        if (isEven) {
            tableRow.setBackgroundColor(ContextCompat.getColor(this, R.color.AliceBlue))
        } else {
            tableRow.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLighterGrey))
        }
        isEven = !isEven

        formNameTV.setLayoutParams(TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 4.0f))
        formNameTV.setText(formName)
        formNameTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f)
        tableRow.addView(formNameTV)

        timesFormFilledTV.setLayoutParams(TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f))
        timesFormFilledTV.setText(filled)
        timesFormFilledTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f)
        timesFormFilledTV.textAlignment = View.TEXT_ALIGNMENT_CENTER
        tableRow.addView(timesFormFilledTV)

        timesFormUploadedTV.setLayoutParams(TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f))
        timesFormUploadedTV.setText(uploaded)
        timesFormUploadedTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f)
        timesFormUploadedTV.textAlignment = View.TEXT_ALIGNMENT_CENTER
        tableRow.addView(timesFormUploadedTV)

        tableLayout.addView(tableRow)
    }

}
