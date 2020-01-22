package ihsinformatics.com.hydra_mobile.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestOrder
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.TestSample
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.ManageTestSample
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.TestSampleAdder
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.TestSampleResult
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.TestSummary
import ihsinformatics.com.hydra_mobile.utils.SessionManager


class CommonLabAdapter(
    val testOrderTypeList: List<LabTestOrder>,
    c: Context,
    applicationContext: Context
) :
    RecyclerView.Adapter<CommonLabAdapter.SingleItemTestHolder>() {

    var testOrderList = testOrderTypeList
    var context: Context = c

    val gson = Gson()
    var sessionManager = SessionManager(context)
    var applicationContext = applicationContext

    override fun onBindViewHolder(holder: SingleItemTestHolder, position: Int) {
        holder?.testtype?.text = testOrderList[position].labTestType!!.name
        holder?.testDescription?.text = testOrderList[position].labReferenceNumber

        if (null == testOrderList[position].labTestSamples || testOrderList[position].labTestSamples.size == 0) {
            holder?.edit.setText("Manage Test")
        } else {
            holder?.edit.setText("Result")
        }



        holder?.summary.setOnClickListener {

            if (null != testOrderList[position].labTestSamples && testOrderList[position].labTestSamples.size != 0) {
                var intent = Intent(context, TestSummary::class.java)
                val dataListJson: String = gson.toJson(testOrderList[position])
                intent.putExtra("lab", dataListJson)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "No Summary to Show", Toast.LENGTH_SHORT).show()
            }

        }



        holder?.edit.setOnClickListener {

            holder.edit.isEnabled = false

            if (testOrderList[position].labTestSamples.size == 0) {
                Toast.makeText(context, "No Test Samples Available", Toast.LENGTH_SHORT).show()
                holder.edit.isEnabled = true
                var intent = Intent(context, TestSampleAdder::class.java)
                intent.putExtra("testOrderID",testOrderList[position].testOrderId)
                context.startActivity(intent)
                //TODO Implement logic for adding test sample
            }else if (checkStatusForTestSample(position)) {
                holder.edit.isEnabled = true
                var intent = Intent(context, TestSampleResult::class.java)
                intent.putExtra("testOrderID",testOrderList[position].testOrderId)
                context.startActivity(intent)
            } else {
                var intent = Intent(context, ManageTestSample::class.java)
                val testSampleListJson: String = gson.toJson(testOrderList[position].labTestSamples)
                intent.putExtra("testSamples", testSampleListJson)
                context.startActivity(intent)
            }


        }
    }


    fun checkStatusForTestSample(position:Int): Boolean {
        if (null != testOrderList[position].labTestSamples && testOrderList[position].labTestSamples.size != 0) {
            for (i in 0 until testOrderList[position].labTestSamples.size) {
                if (!testOrderList[position].labTestSamples.get(i).status.toLowerCase().equals("accepted")) {
                    return false
                } else {
                    Toast.makeText(
                        context,
                        "One of the sample is already accepted",
                        Toast.LENGTH_SHORT
                    ).show()
                    return true
                }

            }
        }
        return false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleItemTestHolder {
        val v = LayoutInflater.from(parent?.context)
            .inflate(R.layout.common_lab_item_layout, parent, false)
        return SingleItemTestHolder(v)

    }

    override fun getItemCount(): Int {
        return testOrderTypeList.size
    }

    fun updateTestOrderList(testOrderOrder: ArrayList<LabTestOrder>) {
        this.testOrderList = testOrderOrder
        notifyDataSetChanged()
    }

    inner class SingleItemTestHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val testtype = itemView.findViewById<TextView>(R.id.testType)
        val testDescription = itemView.findViewById<TextView>(R.id.testDescription)
        val summary = itemView.findViewById<TextView>(R.id.summary)
        val edit = itemView.findViewById<TextView>(R.id.edit)


    }

}


