package ihsinformatics.com.hydra_mobile.ui.adapter


import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.InstructionsAdder
import java.util.*


class CustomExpandableTestAdderAdapter constructor(
    private val context: Context,
    private val titleList: List<String>,
    private val dataList: HashMap<String, ArrayList<String>>

) : BaseExpandableListAdapter() {

    var mCheckBoxData=HashMap<String, String>()

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.dataList[this.titleList[listPosition]]!![expandedListPosition]
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val expandedListText = getChild(listPosition, expandedListPosition) as String
        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.lab_test_added_item_layout, null)
        }

        val reference = convertView!!.findViewById<TextView>(R.id.reference)
        val testType = convertView!!.findViewById<CheckBox>(R.id.testType)
        testType.text = expandedListText

        val tag = Pair(
            getGroupId(listPosition),
            getChildId(listPosition, expandedListPosition)
        )
        testType.tag=tag
        testType.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(arg0: CompoundButton?, value: Boolean) {
                reference.setText("2020-01-20 10:54:47.201")
                mCheckBoxData.put(arg0!!.text.toString(), reference.text.toString());
            }
        })
//        testType.setOnCheckedChangeListener { buttonView, isChecked ->
//            reference.setText("2020-01-20 10:54:47.201")
//            mCheckBoxData.put(convertView.getTag().toString(), );
//
//        }

        val addInstructionsButton = convertView!!.findViewById<TextView>(R.id.addInstructions)

        addInstructionsButton.setOnClickListener {
            context.startActivity(Intent(context, InstructionsAdder::class.java))

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

    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val listTitle = getGroup(listPosition) as String


        if (convertView == null) {
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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


    fun getCheckBoxData(): HashMap<String, String>? {
        return mCheckBoxData
    }

}
