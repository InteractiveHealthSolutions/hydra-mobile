package ihsinformatics.com.hydra_mobile.ui.adapter

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.ComponentFormsObject
import ihsinformatics.com.hydra_mobile.ui.activity.ExpandedFormsActivity
import kotlinx.android.synthetic.main.phase_component_view.view.*


class PhaseComponentAdapter() :
    RecyclerView.Adapter<PhaseComponentAdapter.ViewHolder>() {

    private var componentFormsObjectComponentList: ArrayList<ComponentFormsObject> = ArrayList()
    lateinit var snapHelper: SnapHelper
    var recycledViewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()
    lateinit var context: Context



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhaseComponentAdapter.ViewHolder {
        this.context = parent.context
        val v = LayoutInflater.from(context)
            .inflate(ihsinformatics.com.hydra_mobile.R.layout.phase_component_view, parent, false)
        snapHelper = GravitySnapHelper(Gravity.START)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return componentFormsObjectComponentList.size
    }

    fun setPhaseComponentList(componentFormsObjectComponentList: ArrayList<ComponentFormsObject>) {
        this.componentFormsObjectComponentList = componentFormsObjectComponentList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PhaseComponentAdapter.ViewHolder, position: Int) {
        holder.bindItems(componentFormsObjectComponentList[position])
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val componentName: TextView = itemView.tv_component_name as TextView

        fun bindItems(componentFormsObject: ComponentFormsObject) {

            componentName.text = componentFormsObject.name
            if (componentFormsObject.forms.size <= 3) {
                itemView.img_expend_form.visibility = View.INVISIBLE
            }

            val adapter = FormsListDataAdapter(componentFormsObject.forms, context)
            itemView.rv_form_container.setHasFixedSize(false)
            itemView.rv_form_container.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            itemView.rv_form_container.adapter = adapter
            itemView.rv_form_container.setRecycledViewPool(recycledViewPool)
            snapHelper.attachToRecyclerView(itemView.rv_form_container)

            itemView.img_expend_form.setOnClickListener(View.OnClickListener { view ->
                Constant.formList = componentFormsObject.forms
                Constant.componentName = componentFormsObject.name
                context.startActivity(Intent(context, ExpandedFormsActivity::class.java))
            })
        }

    }
}