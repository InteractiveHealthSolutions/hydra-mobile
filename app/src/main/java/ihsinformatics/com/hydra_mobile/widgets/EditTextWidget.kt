package ihsinformatics.com.hydra_mobile.widgets


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.databinding.ViewDataBinding
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.databinding.ActivitySingleSelectBinding
import ihsinformatics.com.hydra_mobile.databinding.EditTextWidgetBinding

class EditTextWidget:Widgets{


    lateinit var context:Context
    lateinit var binding:EditTextWidgetBinding

    lateinit var id:String
    lateinit var question:String
    var required=false
    var visible=true


    constructor(context: Context, id:String, question:String, required:String) {
        this.context=context
        this.question=question
        this.id=id
        if (required.equals('Y') || required.equals('y'))
            this.required = true
        else
            this.required = false

        init()
    }
    fun init()
    {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_text_widget, null, false)
        binding.question.text=question

    }

    override fun getView(): View {
        return binding.root
    }

    override fun getID():String
    {
        return this.id
    }

    override fun setVisiblity(v:Boolean)
    {
        this.visible=v
    }



    override fun getmyQuestion():String
    {
        return this.question
    }

    override fun getVisibility(): Boolean {
        return this.visible
    }

    override fun getValue(): String {
       return binding.answer.text.toString()
    }

}

