package ihsinformatics.com.hydra_mobile.ui.adapter

import android.R.id.button1
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestOrder
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.ManageTestSample
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.TestSummary


class CommonLabAdapter(val testOrderTypeList: List<LabTestOrder>, c: Context) :
    RecyclerView.Adapter<CommonLabAdapter.SingleItemTestHolder>() {

    var testOrderList = testOrderTypeList
    var context: Context = c

    override fun onBindViewHolder(holder: SingleItemTestHolder, position: Int) {
        holder?.testtype?.text = testOrderList[position].labTestType!!.name
        holder?.testDescription?.text = testOrderList[position].labReferenceNumber

        holder?.summary.setOnClickListener {
            var intent = Intent(
                context,
                TestSummary::class.java
            )

            val gson = Gson()
            val dataListJson: String = gson.toJson(testOrderList[position])
            intent.putExtra("lab", dataListJson)
            context.startActivity(intent)
        }




        holder?.update.setOnClickListener {
            val popup = PopupMenu(context, holder.update)
            //Inflating the Popup using xml file
            //Inflating the Popup using xml file
            popup.getMenuInflater()
                .inflate(R.menu.update_test_menu, popup.getMenu())

            //registering popup with OnMenuItemClickListener
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener,
                PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when(item.getTitle())
                    {
                        context.resources.getString(R.string.manage_test_sample) -> {
                            context.startActivity(Intent(context,ManageTestSample::class.java))
                        }

                        context.resources.getString(R.string.test_result) ->{
                            Toast.makeText(
                                context,
                                "You Clicked : Test Result",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    return true
                }
            })

            popup.show()
        }
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
        val update = itemView.findViewById<TextView>(R.id.update)


    }

}


