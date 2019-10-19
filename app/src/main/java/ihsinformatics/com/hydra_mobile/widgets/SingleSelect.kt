package ihsinformatics.com.hydra_mobile.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.databinding.ActivitySingleSelectBinding
import java.util.*


class SingleSelect : Widgets {

    lateinit var context: Context
    lateinit var binding: ActivitySingleSelectBinding

    lateinit var id: String
    lateinit var question: String
    var required = false
    var visible = true
    lateinit var options: Array<String>

    constructor(context: Context, id: String, question: String, required: String){//, options: Array<String>) {
        this.context = context
        this.question = question
        this.id = id
       // this.options = options
        if (required.equals('Y') || required.equals('y'))
            this.required = true
        else
            this.required = false

        init()
    }


    fun init() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_single_select, null, false)

        binding.question.text = question

        val aa = ArrayAdapter(context, android.R.layout.simple_spinner_item, context.resources.getStringArray(R.array.end_of_followup_for))
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.singleSelect!!.setAdapter(aa)

//        binding.singleSelect.setOnClickListener( {
//
//        })


    }


    override fun getView(): View {
        return binding.root
    }

    override fun getID(): String {
        return this.id
    }

    override fun setVisiblity(v: Boolean) {
        this.visible = v
    }


    override fun getmyQuestion(): String {
        return this.question
    }


    override fun getVisibility(): Boolean {
        return this.visible
    }

    override fun getValue(): String {
        return binding.singleSelect.selectedItem.toString()
    }


}

