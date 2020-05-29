package ihsinformatics.com.hydra_mobile.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess
import com.ihsinformatics.dynamicformsgenerator.data.pojos.UserReports
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.NameAndUUID
import ihsinformatics.com.hydra_mobile.ui.viewmodel.WorkFlowViewModel
import org.jetbrains.anko.padding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class UserReports : AppCompatActivity() {

    private lateinit var tableLayout: TableLayout
    private lateinit var username: TextView
    private lateinit var date: TextView
    private lateinit var spWorkflow: Spinner
    private var workflowsUUIDMapping = HashMap<String, String>()
    private lateinit var workflowsList: List<String>
    private var isEven = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_reports)

        initViews()

    }


    private fun initViews() {

        tableLayout = findViewById(R.id.tableLayout)

        username = findViewById(R.id.username)
        username.setText(Global.USERNAME)

        date = findViewById(R.id.date)
        val dateValue: String = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(Date())
        date.setText(dateValue)

        spWorkflow = findViewById(R.id.workflowsSpinner)
        setValuesInWorkflowSpinner()
        setDefaultSpinnerValueAsCurrentWorkflow()

        spWorkflow.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                tableLayout.removeAllViews()

                val selectedWorkFlow = workflowsUUIDMapping.get(workflowsList.get(position))

                val userReports = DataAccess.getInstance()
                    .getAllUserReportsByUserNameAndWorkflow(this@UserReports, Global.USERNAME, selectedWorkFlow)


                displayDataInTable(userReports)

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

    }

    private fun setDefaultSpinnerValueAsCurrentWorkflow() {

        var workflowName = ""
        for (workflow in workflowsUUIDMapping) {
            if (workflow.value.equals(Global.WORKFLOWUUID)) {
                workflowName = workflow.key
                break;
            }
        }
        spWorkflow.setSelection(workflowsList.indexOf(workflowName))
    }


    private fun setValuesInWorkflowSpinner() {
        val workflowViewModel = ViewModelProviders.of(this).get(WorkFlowViewModel::class.java)
        val workFlows = workflowViewModel.getAllWorkflowsAlongWithUUID()
        convertNameValuePairToHashMap(workFlows)
        workflowsList = workflowsUUIDMapping.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, workflowsList)
        spWorkflow.adapter = adapter
    }


    private fun convertNameValuePairToHashMap(workflows: List<NameAndUUID>) {

        for (workflow in workflows) {
            workflowsUUIDMapping.put(workflow.name, workflow.uuid)
        }
    }


    private fun displayDataInTable(userReports: MutableList<UserReports>) {

        setDataInTableRow("Form Name", "Filled","Uploaded", ContextCompat.getColor(this, R.color.colorGrey),ContextCompat.getColor(this, R.color.colorWhite))

        for (report in userReports) {
            if (isEven) {
                setDataInTableRow(report.encounter, "1", report.encounter_uploaded.toString(), ContextCompat.getColor(this, R.color.AliceBlue),ContextCompat.getColor(this, R.color.colorGrey))
            } else {
                setDataInTableRow(report.encounter, "1", report.encounter_uploaded.toString(), ContextCompat.getColor(this, R.color.colorLighterGrey),ContextCompat.getColor(this, R.color.colorGrey))
            }
            isEven = !isEven
        }
    }

    private fun setDataInTableRow(formName: String, filled: String, uploaded: String, color: Int, textColor: Int) {
        val formNameTV = TextView(this)
        val timesFormFilledTV = TextView(this)
        val timesFormUploadedTV = TextView(this)

        val tableRow = TableRow(this)
        tableRow.padding = 5
        tableRow.setBackgroundColor(color)

        formNameTV.setLayoutParams(TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 4.0f))
        formNameTV.setText(formName)
        formNameTV.setTextColor(textColor)
        formNameTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f)
        tableRow.addView(formNameTV)

        timesFormFilledTV.setLayoutParams(TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f))
        timesFormFilledTV.setText(filled)
        timesFormFilledTV.setTextColor(textColor)
        timesFormFilledTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f)
        timesFormFilledTV.textAlignment = View.TEXT_ALIGNMENT_CENTER
        tableRow.addView(timesFormFilledTV)

        timesFormUploadedTV.setLayoutParams(TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f))
        timesFormUploadedTV.setText(uploaded)
        timesFormUploadedTV.setTextColor(textColor)
        timesFormUploadedTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18.0f)
        timesFormUploadedTV.textAlignment = View.TEXT_ALIGNMENT_CENTER
        tableRow.addView(timesFormUploadedTV)

        tableLayout.addView(tableRow)
    }

}
