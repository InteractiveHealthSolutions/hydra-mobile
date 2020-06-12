package ihsinformatics.com.hydra_mobile.ui.adapter


import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.HashMap
import android.widget.*
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.TestSampleApi
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.TestSample
import ihsinformatics.com.hydra_mobile.data.remote.service.CommonLabApiService
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.CommonLabActivity
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CustomExpandableManageTestSampleAdapter internal constructor(private val context: Context, private val titleList: List<String>, private val dataList: HashMap<String, ArrayList<TestSample>>, testSampleConcepts: CommonLabApiService) : BaseExpandableListAdapter() {

    val testSampleConceptAdder=testSampleConcepts
    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.dataList[this.titleList[listPosition]]!![expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(listPosition: Int, expandedListPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val expandedListText = getChild(listPosition, expandedListPosition) as TestSample
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.manage_test_sample_item_layout, null)
        }
        val specimenType = convertView!!.findViewById<TextView>(R.id.specimenType)
        val specimenSite = convertView!!.findViewById<TextView>(R.id.specimenSite)
        val acceptButton = convertView!!.findViewById<TextView>(R.id.accept)
        val rejectButton = convertView!!.findViewById<TextView>(R.id.reject)

        specimenType.text = expandedListText.specimenType.display
        specimenSite.text = expandedListText.specimenSite.display

        acceptButton.setOnClickListener{
            acceptRejectSample(expandedListText,"ACCEPTED")

        }

        rejectButton.setOnClickListener{
            acceptRejectSample(expandedListText,"REJECTED")
        }


        return convertView
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return this.dataList[this.titleList[listPosition]]!!.size
    }

    override fun getGroup(listPosition: Int): Any {
        return this.titleList[listPosition]
    }

    override fun getGroupCount(): Int {
        return this.titleList.size
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getGroupView(listPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val listTitle = getGroup(listPosition) as String


        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.add_test_list_item, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.listTitle)
        val arrowImage = convertView!!.findViewById<ImageView>(R.id.arrow)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle




        if(isExpanded)
        {
            arrowImage.setImageDrawable(context.resources.getDrawable(R.drawable.ic_up_arrow))
        }
        else
        {
            arrowImage.setImageDrawable(context.resources.getDrawable(R.drawable.ic_arrow_down))
        }
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }


    fun acceptRejectSample(expandedListText: TestSample,status:String)
    {

        var sendParams = JSONObject()
        sendParams.put("labTest", expandedListText.labTest.uuid)
        sendParams.put("labTestSampleId", expandedListText.labTestSampleId)
        sendParams.put("specimenType", expandedListText.specimenType.uuid)
        sendParams.put("specimenSite", expandedListText.specimenSite.uuid)
        sendParams.put("quantity", expandedListText.quantity.toString())
        sendParams.put("sampleIdentifier", expandedListText.sampleIdentifier.toString())
        sendParams.put("collectionDate", expandedListText.collectionDate)
        sendParams.put("collector", expandedListText.collector.uuid)
        sendParams.put("expiryDate", expandedListText.expiryDate)
        sendParams.put("status", status)
        sendParams.put("comments", expandedListText.comments)


        val body: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), sendParams.toString())


        testSampleConceptAdder.addTestSample(body).enqueue(object : Callback<TestSampleApi> {
            override fun onFailure(call: Call<TestSampleApi>, t: Throwable) {

                ToastyWidget.getInstance().displayError(context, context.getString(R.string.internet_issue), Toast.LENGTH_SHORT)
                context.startActivity(Intent(context, CommonLabActivity::class.java))

            }

            override fun onResponse(
                call: Call<TestSampleApi>, response: Response<TestSampleApi>
                                   ) {
                if (response.isSuccessful) {
                    ToastyWidget.getInstance().displaySuccess(context, "Test Sample Status Changed Successfully", Toast.LENGTH_SHORT)
                    context.startActivity(Intent(context, CommonLabActivity::class.java))
                } else {
                    ToastyWidget.getInstance().displayError(context, "Error Changing Test Sample Status", Toast.LENGTH_SHORT)
                }
            }

        })
    }

}
