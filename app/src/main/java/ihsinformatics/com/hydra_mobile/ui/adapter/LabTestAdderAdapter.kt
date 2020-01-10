package ihsinformatics.com.hydra_mobile.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.CommonLabModel
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.TestSummary

class LabTestAdderAdapter(val testTypeList: ArrayList<CommonLabModel>, c: Context) :
    RecyclerView.Adapter<LabTestAdderAdapter.SingleItemTestHolder>() {

    var context: Context = c

    override fun onBindViewHolder(holder: SingleItemTestHolder, position: Int) {
        holder?.testtype?.text = testTypeList[position].testType
        holder?.testDescription?.text = testTypeList[position].testDescription
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleItemTestHolder {
        val v = LayoutInflater.from(parent?.context)
            .inflate(R.layout.lab_test_added_item_layout, parent, false)
        return SingleItemTestHolder(v)

    }

    override fun getItemCount(): Int {
        return testTypeList.size
    }

    inner class SingleItemTestHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val testtype = itemView.findViewById<TextView>(R.id.testType)
        val testDescription = itemView.findViewById<TextView>(R.id.testDescription)
        val manageTest = itemView.findViewById<CardView>(R.id.manageTest)

        init {
            itemView.setOnClickListener {
                context.startActivity(
                    Intent(
                        context,
                        TestSummary::class.java
                    )
                )
            }
        }

    }


}