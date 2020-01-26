package ihsinformatics.com.hydra_mobile.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
    val testOrderTypeList: List<LabTestOrder>, c: Context, applicationContext: Context
                      ) : RecyclerView.Adapter<CommonLabAdapter.SingleItemTestHolder>() {

    var testOrderList = testOrderTypeList
    var context: Context = c

    val gson = Gson()
    var sessionManager = SessionManager(context)
    var applicationContext = applicationContext

    override fun onBindViewHolder(holder: SingleItemTestHolder, position: Int) {
        holder.testtype?.text = testOrderList[position].labTestType.name
        holder.testDescription?.text = testOrderList[position].labReferenceNumber


        if (testOrderList[position].labTestType.requiresSpecimen && testOrderList[position].labTestSamples.size == 0) {
            holder.edit.setText("Add Test Sample")
        } else if (testOrderList[position].labTestSamples.size != 0 && testOrderList[position].labTestType.requiresSpecimen && !checkStatusForTestSample(position)) {
            holder.edit.setText("Manage Test")
        } else if (!testOrderList[position].labTestType.requiresSpecimen || checkStatusForTestSample(position)) {
            holder.edit.setText("Add Result")
        }


        //TODO Add completed tag on recycler view where result is submitted (for now it can not be donw because attribute field is not enabled in api) ~Taha
//        for (i in 0 until testOrderList[position].labTestSamples.size) {
//            if (testOrderList[position].labTestSamples.get(i).status.toLowerCase().equals("accepted")) {
//                holder.completedTag.visibility = View.VISIBLE
//            }
//        }


        holder?.summary.setOnClickListener {
            //TODO API doesnot return fields for summary (attribute) so cant show proper summary for now ~Taha
            if (null != testOrderList[position].labTestSamples && testOrderList[position].labTestSamples.size != 0 && checkStatusForTestSample(position)) {
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

            if (testOrderList[position].labTestType.requiresSpecimen && testOrderList[position].labTestSamples.size == 0) {
                Toast.makeText(context, "No Test Samples Available", Toast.LENGTH_SHORT).show()
                holder.edit.isEnabled = true
                var intent = Intent(context, TestSampleAdder::class.java)
                intent.putExtra("labTest", testOrderList[position].uuid)

                context.startActivity(intent)

            } else if (testOrderList[position].labTestSamples.size != 0 && testOrderList[position].labTestType.requiresSpecimen && !checkStatusForTestSample(position)) {

                var intent = Intent(context, ManageTestSample::class.java)
                val testSampleListJson: String = gson.toJson(testOrderList[position].labTestSamples)
                intent.putExtra("testSamples", testSampleListJson)
                intent.putExtra("labTest", testOrderList[position].uuid)
                holder.edit.isEnabled = true
                context.startActivity(intent)

            } else if (!testOrderList[position].labTestType.requiresSpecimen || checkStatusForTestSample(position)) {

                holder.edit.isEnabled = true
                var intent = Intent(context, TestSampleResult::class.java)
                intent.putExtra("testTypeUuid", testOrderList[position].labTestType.uuid)
                intent.putExtra("OrderUUID", testOrderList[position].order.uuid)
                intent.putExtra("labReferenceNumber", testOrderList[position].labReferenceNumber)
                intent.putExtra("labTestType", testOrderList[position].labTestType.uuid)



                context.startActivity(intent)
            }


        }
    }


    //returns true for accepted
    fun checkStatusForTestSample(position: Int): Boolean {
        for (i in 0 until testOrderList[position].labTestSamples.size) {
            if (testOrderList[position].labTestSamples.get(i).status.toLowerCase().equals("accepted")) {

                return true
            }
        }

        return false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleItemTestHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.common_lab_item_layout, parent, false)
        return SingleItemTestHolder(v)

    }

    override fun getItemCount(): Int {
        return testOrderTypeList.size
    }

    inner class SingleItemTestHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val testtype = itemView.findViewById<TextView>(R.id.testType)
        val testDescription = itemView.findViewById<TextView>(R.id.testDescription)
        val summary = itemView.findViewById<TextView>(R.id.summary)
        val edit = itemView.findViewById<TextView>(R.id.edit)
        val completedTag = itemView.findViewById<LinearLayout>(R.id.completedTag)

    }

}


