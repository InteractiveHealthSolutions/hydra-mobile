package ihsinformatics.com.hydra_mobile.ui.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.CommonLabModel

class CustomRecycleViewAdapter (val testTypeList: ArrayList<CommonLabModel>): RecyclerView.Adapter<CustomRecycleViewAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: CustomRecycleViewAdapter.ViewHolder, position: Int) {
        holder?.testtype?.text = testTypeList[position].testType
        holder?.testDescription?.text = testTypeList[position].testDescription
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomRecycleViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.common_lab_item_layout, parent, false)
        return ViewHolder(v);

    }

    override fun getItemCount(): Int {
        return testTypeList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val testtype = itemView.findViewById<TextView>(R.id.testType)
        val testDescription = itemView.findViewById<TextView>(R.id.testDescription)

    }



}