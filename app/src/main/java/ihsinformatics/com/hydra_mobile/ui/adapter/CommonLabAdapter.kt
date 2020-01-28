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
import com.ihsinformatics.dynamicformsgenerator.screens.dialogs.NetworkProgressDialog
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.AttributeResponse
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.AttributesApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestOrder
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.TestSample
import ihsinformatics.com.hydra_mobile.data.remote.service.CommonLabApiService
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.*
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class CommonLabAdapter(
    val testOrderTypeList: List<LabTestOrder>, c: Context, applicationContext: Context
                      ) : RecyclerView.Adapter<CommonLabAdapter.SingleItemTestHolder>() {

    var testOrderList = testOrderTypeList
    var context: Context = c

    val networkProgressDialog = NetworkProgressDialog(context)

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

            val labTestAttributesGetter = RequestManager(applicationContext, sessionManager.getUsername(), sessionManager.getPassword()).getPatientRetrofit().create(CommonLabApiService::class.java)
            networkProgressDialog.show()


            labTestAttributesGetter.getLabTestAttribute(testOrderList[position].testOrderId, Constant.REPRESENTATION).enqueue(object : Callback<AttributeResponse> {
                override fun onResponse(
                    call: Call<AttributeResponse>, response: Response<AttributeResponse>
                                       ) {
                    Timber.e(response.message())
                    if (response.isSuccessful) {

                        var intent = Intent(context, TestSummary::class.java)
                        if (testOrderList != null) {
                            val dataListJson: String = gson.toJson(testOrderList[position])
                            intent.putExtra("lab", dataListJson)
                        } else {
                            intent.putExtra("lab", "")
                        }

                        val attributesJson: String = gson.toJson(response.body()!!.attributes)
                        intent.putExtra("attributes",attributesJson)

                        networkProgressDialog.dismiss()
                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        context.startActivity(intent)

                    } else {
                        networkProgressDialog.dismiss()
                        ToastyWidget.getInstance().displayError(context, "Error fetching Test Results Attributes", Toast.LENGTH_LONG)
                    }


                }

                override fun onFailure(call: Call<AttributeResponse>, t: Throwable) {
                    Timber.e(t.localizedMessage)
                    networkProgressDialog.dismiss()
                    ToastyWidget.getInstance().displayError(context, context.getString(R.string.internet_issue), Toast.LENGTH_LONG)
                    context.startActivity(Intent(context, CommonLabActivity::class.java))
                }

            })


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


