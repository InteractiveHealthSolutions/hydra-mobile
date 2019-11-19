package ihsinformatics.com.hydra_mobile.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.TextView
import android.widget.Toast
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import com.ihsinformatics.dynamicformsgenerator.utils.Global.patientData
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_profile.*
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.PeriodType
import org.json.JSONObject
import android.util.DisplayMetrics




class ProfileActivity : BaseActivity() {

    lateinit var tvID: TextView;
    lateinit var tvAge: TextView;
    lateinit var tvName: TextView;

    internal var expandableListView: ExpandableListView? = null
    internal var adapter: ExpandableListAdapter? = null
    internal var titleList: List<String>? = null


    val data: HashMap<String, List<String>>
        get() {
            val listData = HashMap<String, List<String>>()

            val access = DataAccess.getInstance()
            val existineOfflinePatient = access.getPatientByMRNumber(this, patientData.getPatient().getIdentifier())

            val patientSource = ArrayList<String>()
            val patientType = ArrayList<String>()
            val patientRiskCategory = ArrayList<String>()
            val diagnosis = ArrayList<String>()
            val outcome = ArrayList<String>()
            val vitals = ArrayList<String>()
            val relationships = ArrayList<String>()
            val recentVisits = ArrayList<String>()
            val nextTBAppointment = ArrayList<String>()


            if (existineOfflinePatient != null) {
                var fieldJsonString: String? = existineOfflinePatient.fieldDataJson
                if (fieldJsonString == null) fieldJsonString = JSONObject().toString()
                val existingFieldsJson = JSONObject(fieldJsonString)


                var temp = existingFieldsJson.get("patientSource").toString()
                if (temp != null && temp != "") {
                    patientSource.add(temp)
                }

                temp = existingFieldsJson.get("patientType").toString()
                if (temp != null && temp != "") {
                    patientType.add(temp)
                }


                temp = existingFieldsJson.get("patientRiskCategory").toString()
                if (temp != null && temp != "") {
                    patientRiskCategory.add(temp)
                }


                temp = existingFieldsJson.get("diseaseSite").toString()
                if (temp != null && temp != "") {
                    diagnosis.add(temp)
                }

                temp = existingFieldsJson.get("confirmationType").toString()
                if (temp != null && temp != "") {
                    diagnosis.add(temp)
                }

                temp = existingFieldsJson.get("tbType").toString()
                if (temp != null && temp != "") {
                    diagnosis.add(temp)
                }

                temp = existingFieldsJson.get("endOfFollowUpFor").toString()
                if (temp != null && temp != "") {
                    outcome.add(temp)
                }

                temp = existingFieldsJson.get("relatedOutcome").toString()
                if (temp != null && temp != "") {
                    outcome.add(temp)
                }

                temp = existingFieldsJson.get("weight").toString()
                if (temp != null && temp != "") {
                    vitals.add("Weight: "+temp)
                }

                temp = existingFieldsJson.get("height").toString()
                if (temp != null && temp != "") {
                    vitals.add("Height: "+temp)
                }

                temp = existingFieldsJson.get("bmi").toString()
                if (temp != null && temp != "") {
                    vitals.add("BMI: "+temp)
                }

                temp = existingFieldsJson.get("nextTBAppointment").toString()
                if (temp != null && temp != "") {
                    nextTBAppointment.add(temp)
                }


                listData.put("Patient Source", patientSource)
                listData.put("Patient Type", patientType)
                listData.put("Patient Risk Category", patientRiskCategory)
                listData.put("Diagnosis", diagnosis)
                listData.put("Outcome", outcome)
                listData.put("Vitals", vitals)
                listData.put("Relationships", relationships)
                listData.put("Recent Visits", recentVisits)
                listData.put("Next TB Appointment", nextTBAppointment)


            }

            return listData
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = ""
        setContentView(ihsinformatics.com.hydra_mobile.R.layout.activity_expandable_profile)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        expandableListView = findViewById(ihsinformatics.com.hydra_mobile.R.id.expandableListView)

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val width = metrics.widthPixels

        expandableListView!!.setIndicatorBounds(width - GetPixelFromDips(50f), width - GetPixelFromDips(10f));


        tvID = findViewById(ihsinformatics.com.hydra_mobile.R.id.tvId)
        tvAge = findViewById(ihsinformatics.com.hydra_mobile.R.id.tvAge)
        tvName = findViewById(ihsinformatics.com.hydra_mobile.R.id.tvName)

        if (Global.patientData != null) {
            var identifiers = ""
            val ids = Global.patientData.getIdentifiers()
            if (ids != null) {
                val it = ids!!.keys.iterator()
                while (it.hasNext()) {
                    val key = it.next()
                    val value = ids!!.get(key)
                    identifiers += value/*+", "*/
                }
            }
            tvID.setText(identifiers)

            // tvID.setText(Global.patientData.patient.identifier)


            val birthDate = Global.patientData.getPatient().getBirthDate()
            val birthTime = DateTime(birthDate)
            val nowTime = DateTime()


            val interval = Interval(birthTime, nowTime)
            val period = interval.toPeriod().normalizedStandard(PeriodType.yearMonthDay())
            val years = period.getYears()
            val months = period.getMonths()
            val days = period.getDays()
            tvAge?.setText(years.toString() + " years, " + months.toString() + " months, " + days.toString() + " days")
            // tvAge.setText(Global.patientData.patient.age.toString())
            tvName.setText(Global.patientData.patient.givenName)

        }



        if (expandableListView != null) {
            val listData = data
            titleList = ArrayList(listData.keys)
            adapter = CustomExpandableListAdapter(this, titleList as ArrayList<String>, listData)
            expandableListView!!.setAdapter(adapter)

            //expandableListView!!.setOnGroupClickListener{parent,v, i,l->setListViewHeight(parent,i)}

            expandableListView!!.setOnGroupExpandListener { groupPosition ->
//                Toast.makeText(
//                    applicationContext,
//                    (titleList as ArrayList<String>)[groupPosition] + " List Expanded.",
//                    Toast.LENGTH_SHORT
//                ).show()
            }

            expandableListView!!.setOnGroupCollapseListener { groupPosition ->
//                Toast.makeText(
//                    applicationContext,
//                    (titleList as ArrayList<String>)[groupPosition] + " List Collapsed.",
//                    Toast.LENGTH_SHORT
//                ).show()
            }

            expandableListView!!.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
//                Toast.makeText(
//                    applicationContext,
//                    "Clicked: " + (titleList as ArrayList<String>)[groupPosition] + " -> " + listData[(titleList as ArrayList<String>)[groupPosition]]!!.get(
//                        childPosition
//                    ),
//                    Toast.LENGTH_SHORT
//                ).show()
                false
            }
        }

    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(ihsinformatics.com.hydra_mobile.R.anim.slide_from_left, ihsinformatics.com.hydra_mobile.R.anim.slide_to_right)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
        super.onBackPressed()

    }


    fun GetPixelFromDips(pixels: Float): Int {
        // Get the screen's density scale
        val scale = resources.displayMetrics.density
        // Convert the dps to pixels, based on density scale
        return (pixels * scale + 0.5f).toInt()
    }

}
