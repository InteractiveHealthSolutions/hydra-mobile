package ihsinformatics.com.hydra_mobile.ui.adapter


import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.HashMap
import android.opengl.ETC1.getWidth
import android.widget.*
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames
import ihsinformatics.com.hydra_mobile.R


class CustomExpandableListAdapter internal constructor(private val context: Context, private val titleList: List<String>, private val dataList: HashMap<String, List<String>>) : BaseExpandableListAdapter() {

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.dataList[this.titleList[listPosition]]!![expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(listPosition: Int, expandedListPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val expandedListText = getChild(listPosition, expandedListPosition) as String
        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(ihsinformatics.com.hydra_mobile.R.layout.activity_listitems, null)
        }
        val expandedListTextView = convertView!!.findViewById<TextView>(ihsinformatics.com.hydra_mobile.R.id.expandedListItem)
        expandedListTextView.text = expandedListText
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

        var imageId = R.drawable.ic_diagonosis
        imageId = when (listTitle) {
            "Recent Visits" -> R.drawable.ic_recent_visit
            "Relationships" -> R.drawable.ic_relation
            "Patient Risk Category" -> R.drawable.ic_allergy
            "Patient Type" -> R.drawable.ic_patient
            "Diagnosis" -> R.drawable.ic_diagonosis
            "Next TB Appointment" -> R.drawable.ic_appointment
            "Patient Source" -> R.drawable.ic_patient_source
            "Outcome" -> R.drawable.ic_outcome
            "Vitals" -> R.drawable.vitals


            else -> R.drawable.ic_diagonosis
        }

        if (convertView == null) {
            val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(ihsinformatics.com.hydra_mobile.R.layout.list_group, null)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(ihsinformatics.com.hydra_mobile.R.id.listTitle)
       // val arrowImage = convertView!!.findViewById<ImageView>(ihsinformatics.com.hydra_mobile.R.id.arrow)
        val listTitleImage = convertView!!.findViewById<ImageView>(ihsinformatics.com.hydra_mobile.R.id.imageTitle)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle

        listTitleImage.setImageDrawable(context.resources.getDrawable(imageId))

//        if(isExpanded)
//        {
//            arrowImage.setImageDrawable(context.resources.getDrawable(R.drawable.ic_up_arrow))
//        }
//        else
//        {
//            arrowImage.setImageDrawable(context.resources.getDrawable(R.drawable.ic_arrow_down))
//        }
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }



}
