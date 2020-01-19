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
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestOrder
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.TestSample
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.TestSampleApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.service.CommonLabApiService
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.ManageTestSample
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.TestSampleResult
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.TestSummary
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


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
    var testTypeList = ArrayList<TestSample>()

    override fun onBindViewHolder(holder: SingleItemTestHolder, position: Int) {
        holder?.testtype?.text = testOrderList[position].labTestType!!.name
        holder?.testDescription?.text = testOrderList[position].labReferenceNumber



        holder?.summary.setOnClickListener {
            var intent = Intent(context, TestSummary::class.java)
            val dataListJson: String = gson.toJson(testOrderList[position])
            intent.putExtra("lab", dataListJson)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }




        holder?.edit.setOnClickListener {

            holder.edit.isEnabled=false

            val testOrderSearch = RequestManager(
                applicationContext, sessionManager.getUsername(),
                sessionManager.getPassword()
            ).getPatientRetrofit().create(CommonLabApiService::class.java)

            testOrderSearch.getTestSampleByLabTestUUID(
                testOrderList[position].uuid,
                Constant.REPRESENTATION
            )
                .enqueue(object : Callback<TestSampleApiResponse> {
                    override fun onResponse(
                        call: Call<TestSampleApiResponse>,
                        response: Response<TestSampleApiResponse>
                    ) {
                        Timber.e(response.message())
                        if (response.isSuccessful) {
                            testTypeList.clear()
                            testTypeList = response.body()!!.testSamples

                            if (testTypeList.size == 0) {
                                Toast.makeText(context,"No Test Samples Available",Toast.LENGTH_SHORT).show()
                                holder.edit.isEnabled=true
                                //TODO Implement logic for adding test sample
                            }

                            else if(checkStatusForTestSample())
                            {
                                holder.edit.isEnabled=true
                                context.startActivity(Intent(context, TestSampleResult::class.java))
                            }
                            else
                            {
                                var intent = Intent(context, ManageTestSample::class.java)
                                val testSampleListJson: String = gson.toJson(testTypeList)
                                intent.putExtra("testSamples", testSampleListJson)
                                context.startActivity(intent)
                            }
                        }
                    }

                    override fun onFailure(call: Call<TestSampleApiResponse>, t: Throwable) {
                        Timber.e(t.localizedMessage)


                        holder.edit.isEnabled=true
                    }

                })


        }
    }


    fun checkStatusForTestSample():Boolean
    {
        if(null!=testTypeList && testTypeList.size!=0)
        {
            for(i in 0 until testTypeList.size)
            {
                if(!testTypeList.get(i).status.toLowerCase().equals("accepted"))
                {
                   return false
                }
                else
                {
                    Toast.makeText(context,"One of the sample is already accepted",Toast.LENGTH_SHORT).show()
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


