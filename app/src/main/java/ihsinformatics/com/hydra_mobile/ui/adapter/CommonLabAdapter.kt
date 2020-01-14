package ihsinformatics.com.hydra_mobile.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.TestOrderModel
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.TestSummary

class CommonLabAdapter (val testOrderTypeList: List<TestOrderModel>, c: Context): RecyclerView.Adapter<CommonLabAdapter.SingleItemTestHolder>() {

    var testOrderList=testOrderTypeList
    var context:Context=c

    override fun onBindViewHolder(holder: SingleItemTestHolder, position: Int) {
        holder?.testtype?.text = testOrderList[position].labTestType.name
        holder?.testDescription?.text = testOrderList[position].labReferenceNumber

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleItemTestHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.common_lab_item_layout, parent, false)
        return SingleItemTestHolder(v)

    }

    override fun getItemCount(): Int {
        return testOrderTypeList.size
    }

    fun updateTestOrderList(testOrderOrder: ArrayList<TestOrderModel>) {
        this.testOrderList = testOrderOrder
        notifyDataSetChanged()
    }

    inner class SingleItemTestHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val testtype = itemView.findViewById<TextView>(R.id.testType)
        val testDescription = itemView.findViewById<TextView>(R.id.testDescription)
        val summary = itemView.findViewById<TextView>(R.id.summary)

        init {
            summary.setOnClickListener{
                context.startActivity(Intent(context,
                    TestSummary::class.java))
            }
        }

    }



}