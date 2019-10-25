package ihsinformatics.com.hydra_mobile.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_profile.*
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.PeriodType

class ProfileActivity : BaseActivity() {

     lateinit var tvID: TextView;
     lateinit var tvAge: TextView;
     lateinit var tvName: TextView;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = ""
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        tvID=findViewById(R.id.tvId)
        tvAge=findViewById(R.id.tvAge)
        tvName=findViewById(R.id.tvName)

        if(Global.patientData!=null)
        {
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



        initView()
    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    private fun initView() {
       // findViewById<TextView>(R.id.tvId).setText(Global.patientData.identifiers.get(ParamNames.MR_NUMBER))
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
}
