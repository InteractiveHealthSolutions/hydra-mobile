package ihsinformatics.com.hydra_mobile.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ihsinformatics.com.hydra_mobile.R


class ManageTestSampleAdapter(val testOrderTypeList: List<String>, c: Context) :
    RecyclerView.Adapter<ManageTestSampleAdapter.SingleItemTestHolder>() {

    var testOrderList = testOrderTypeList
    var context: Context = c

    override fun onBindViewHolder(holder: SingleItemTestHolder, position: Int) {
        holder?.specimenType?.text = testOrderList[position]
        holder?.specimenSite?.text = testOrderList[position]


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleItemTestHolder {
        val v = LayoutInflater.from(parent?.context)
            .inflate(R.layout.manage_test_sample_item_layout, parent, false)
        return SingleItemTestHolder(v)

    }

    override fun getItemCount(): Int {
        return testOrderTypeList.size
    }

    fun updateTestOrderList(testOrderOrder: ArrayList<String>) {
        this.testOrderList = testOrderOrder
        notifyDataSetChanged()
    }

    inner class SingleItemTestHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val specimenType = itemView.findViewById<TextView>(R.id.specimenType)
        val specimenSite = itemView.findViewById<TextView>(R.id.specimenSite)
        val accept = itemView.findViewById<TextView>(R.id.accept)
        val reject = itemView.findViewById<TextView>(R.id.reject)


    }

}


