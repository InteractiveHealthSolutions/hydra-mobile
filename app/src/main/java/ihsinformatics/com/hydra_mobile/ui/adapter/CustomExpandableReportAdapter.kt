package ihsinformatics.com.hydra_mobile.ui.adapter


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.model.Reports.Ob
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.InstructionsAdder
import kotlinx.android.synthetic.main.summary_viewer.view.*
import org.json.JSONObject
import kotlin.collections.HashMap


class CustomExpandableReportAdapter(
    private val context: Context, private val titleList: List<String>, private val dataList: HashMap<String, List<Ob>>
                                   ) : BaseExpandableListAdapter() {


    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.dataList[this.titleList[listPosition]]!![expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(
        listPosition: Int, expandedListPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup
                             ): View {
        var convertView = convertView
        val expandedListText = getChild(listPosition, expandedListPosition) as Ob
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.report_obs_item_layout, null)
        }

        val obsValue = convertView!!.findViewById<LinearLayout>(R.id.obsLayout)
        val displayObs = convertView!!.findViewById<TextView>(R.id.displayObs)

        displayObs.setText(expandedListText.display)



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

    override fun getGroupView(
        listPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup
                             ): View {
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




        if (isExpanded) {
            arrowImage.setImageDrawable(context.resources.getDrawable(R.drawable.ic_up_arrow))
        } else {
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


}
