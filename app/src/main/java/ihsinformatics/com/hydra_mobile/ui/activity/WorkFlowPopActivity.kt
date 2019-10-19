package ihsinformatics.com.hydra_mobile.ui.activity

import android.app.AlertDialog
import android.os.Bundle
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatDialogFragment
import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.data.local.dao.workflow.WorkFlowDao
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.WorkFlow
import ihsinformatics.com.hydra_mobile.ui.viewmodel.WorkFlowViewModel
import androidx.lifecycle.Observer



class WorkFlowPopActivity : AppCompatDialogFragment() {
    private var rg_workFlow: RadioGroup? = null
    private var listener: WorkFlowDialogListener? = null



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.activity_select_workflow, null)

        aesi(view)

        builder.setView(view)
            .setTitle("Login")
            .setNegativeButton("cancel", DialogInterface.OnClickListener { dialogInterface, i -> })
            .setPositiveButton("ok", DialogInterface.OnClickListener { dialogInterface, i ->


                var selectedVal:String=""
                val selectedID=rg_workFlow?.checkedRadioButtonId
                if(selectedID!=-1)
                {
                    var selectedRadioID= selectedID?.let { view.findViewById<RadioButton>(it) }
                    if (selectedRadioID != null) {
                        selectedVal= selectedRadioID.text as String
                        listener!!.applyTexts(selectedVal)
                    }
                }


            })

        rg_workFlow = view.findViewById(R.id.rg_workFlow)

        return builder.create()
    }

     override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as WorkFlowDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "must implement ExampleDialogListener")
        }

    }

    interface WorkFlowDialogListener {
        fun applyTexts(selectedValue: String)
    }


    fun aesi(view: View)
    {
        val dm= DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(dm)

        var width = dm.widthPixels
        var height = dm.heightPixels

        activity?.window?.setLayout((width *0.8).toInt(),(height*0.6).toInt())


        val scale = resources.displayMetrics.density
        val dpAsPixels = (20 * scale + 0.5f).toInt()


        val workflowViewModel = ViewModelProviders.of(this).get(WorkFlowViewModel::class.java)
        workflowViewModel.getAllPWorkflow().observe(this, Observer<List<WorkFlow>> { worklist ->
            if (worklist.isNotEmpty()) {
                val ll_main = view.findViewById(R.id.ll_main_layout) as LinearLayout
                val rg = RadioGroup(activity)
                rg.orientation = RadioGroup.VERTICAL
                for (element in worklist) {
                    val rb = RadioButton(activity)
                    rb.text = element.name
                    rb.textSize = 20f
                    rb.id = element.id
                    rb.tag = element.id
                    rb.setPadding(dpAsPixels,dpAsPixels,dpAsPixels,dpAsPixels)
                    rg.addView(rb)

                    val viewDivider = View(activity)
                    viewDivider.layoutParams =
                        RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    viewDivider.setBackgroundColor(resources.getColor(R.color.dividerColor));
                    val dividerHeight = (resources.displayMetrics.density * 1).toInt() // 1dp to pixels
                    viewDivider.setLayoutParams(
                        RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            dividerHeight
                        )
                    )
                    rg.addView(viewDivider)

                }
                ll_main.removeAllViews()
                ll_main.addView(rg)
            }
        })

    }
}

