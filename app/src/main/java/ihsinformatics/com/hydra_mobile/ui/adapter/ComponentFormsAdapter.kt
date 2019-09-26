package ihsinformatics.com.hydra_mobile.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Forms
import ihsinformatics.com.hydra_mobile.ui.activity.FormsActivity
import ihsinformatics.com.hydra_mobile.ui.helper.GlideApp


internal class FormsListDataAdapter(private val itemModels: List<Forms>, context: Context) :
    RecyclerView.Adapter<FormsListDataAdapter.SingleItemRowHolder>() {
    var context: Context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleItemRowHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_forms_card, null)
        return SingleItemRowHolder(v)
    }

    override fun onBindViewHolder(holder: SingleItemRowHolder, position: Int) {
        val formModel = itemModels!![position]
        holder.tvTitle.text = formModel.encounterType
        GlideApp.with(context)
            .load("")
            .placeholder(R.drawable.logo_final)
            .into(holder.imgForm);
    }

    override fun getItemCount(): Int {
        return itemModels?.size ?: 0
    }

    inner class SingleItemRowHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvTitle: TextView = itemView.findViewById(R.id.tv_form_name)
        var imgForm: ImageView = itemView.findViewById(R.id.img_form_image)

        init {
            itemView.setOnClickListener {
                Constant.formName = tvTitle.text.toString()
                context.startActivity(Intent(context, FormsActivity::class.java))
            }
        }
    }

}
