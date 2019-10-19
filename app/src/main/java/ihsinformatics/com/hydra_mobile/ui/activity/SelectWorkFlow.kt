package ihsinformatics.com.hydra_mobile.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.WorkFlow
import ihsinformatics.com.hydra_mobile.ui.viewmodel.WorkFlowViewModel
import android.view.ViewGroup
import android.widget.*
import ihsinformatics.com.hydra_mobile.R
import androidx.databinding.DataBindingUtil
import ihsinformatics.com.hydra_mobile.databinding.ActivitySelectWorkflowBinding

class SelectWorkFlow : AppCompatActivity() {

    lateinit var binding: ActivitySelectWorkflowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_workflow)

        val dm= DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        var width = dm.widthPixels
        var height = dm.heightPixels

        window.setLayout((width *0.8).toInt(),(height*0.6).toInt())


        val scale = resources.displayMetrics.density
        val dpAsPixels = (20 * scale + 0.5f).toInt()


        val workflowViewModel = ViewModelProviders.of(this).get(WorkFlowViewModel::class.java)
         workflowViewModel.getAllPWorkflow().observe(this, Observer<List<WorkFlow>> { worklist ->
             if (worklist.isNotEmpty()) {
                 //val ll_main = findViewById(R.id.ll_main_layout) as LinearLayout
                 val rg = findViewById(R.id.rg_workflow) as RadioGroup
                 rg.removeAllViews()
                 for (element in worklist) {
                     val rb = RadioButton(this)
                     rb.text = element.name
                     rb.textSize = 20f
                     rb.id = element.id
                     rb.tag = element.id
                     rb.setPadding(dpAsPixels,dpAsPixels,dpAsPixels,dpAsPixels)
                     rg.addView(rb)

                     val viewDivider = View(this)
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
                 //ll_main.removeAllViews()
                 //ll_main.addView(rg)
             }
         })


        binding.submitWorkflow.setOnClickListener(View.OnClickListener {
            var selectedVal:String=""
            val selectedID=binding.rgWorkflow?.checkedRadioButtonId
            if(selectedID!=-1)
            {
                var selectedRadioID= selectedID?.let { findViewById<RadioButton>(it) }
                if (selectedRadioID != null) {
                    selectedVal= selectedRadioID.text as String
                    val returnIntent = Intent()
                    returnIntent.putExtra("result", selectedVal)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }


            }
        })

    }





}


