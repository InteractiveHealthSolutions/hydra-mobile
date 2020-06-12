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
import android.view.MotionEvent
import android.view.WindowManager
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.utils.GlobalPreferences


class SelectWorkFlow : AppCompatActivity() {

    lateinit var binding: ActivitySelectWorkflowBinding
    lateinit var workflows:List<WorkFlow>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_workflow)

        val dm= DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        var width = dm.widthPixels
        var height = dm.heightPixels

        window.setLayout((width *0.8).toInt(),(height*0.6).toInt())

       // window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        this.setFinishOnTouchOutside(false);

        val scale = resources.displayMetrics.density
        val dpAsPixels = (20 * scale + 0.5f).toInt()


        val workflowViewModel = ViewModelProviders.of(this).get(WorkFlowViewModel::class.java)
         workflowViewModel.getAllWorkflowLiveData().observe(this, Observer<List<WorkFlow>> { worklist ->
             if (worklist.isNotEmpty()) {
                 //val ll_main = findViewById(R.id.ll_main_layout) as LinearLayout
                 workflows=ArrayList<WorkFlow>(worklist)
                 val rg = findViewById(R.id.rg_workflow) as RadioGroup
                 rg.removeAllViews()
                 for (element in worklist) {
                     val rb = RadioButton(this)
                     rb.text = element.name
                     rb.textSize = 20f
                     rb.id = element.workflowId
                     rb.tag = element.workflowId
                     rb.setPadding(dpAsPixels,dpAsPixels,dpAsPixels,dpAsPixels)
                     rg.addView(rb)

                     val viewDivider = View(this)
                     viewDivider.layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) as ViewGroup.LayoutParams?
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

             }else
             {
                 ToastyWidget.getInstance().displayError(this,getString(R.string.need_to_sync),Toast.LENGTH_LONG)
                 startActivity(Intent(this,LoginActivity::class.java))
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
                    for(i in workflows)
                    {

                        if(selectedVal.equals(i.name))
                        {
                            GlobalPreferences.getinstance(this).addOrUpdatePreference(GlobalPreferences.KEY.WORKFLOWUUID, i.uuid)
                            Global.WORKFLOWUUID=i.uuid
                        }
                    }

                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }


            }
        })

    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        // If we've received a touch notification that the user has touched
        // outside the app, finish the activity.
        if (MotionEvent.ACTION_OUTSIDE == event.action) {
            finish()
            return true
        }

        // Delegate everything else to Activity.
        return super.onTouchEvent(event)
    }

    override fun onBackPressed() {
       // super.onBackPressed()
        this.setFinishOnTouchOutside(false);

    }
}


