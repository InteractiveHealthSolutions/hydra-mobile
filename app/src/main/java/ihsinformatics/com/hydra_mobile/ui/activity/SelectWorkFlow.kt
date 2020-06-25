package ihsinformatics.com.hydra_mobile.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.WorkFlow
import ihsinformatics.com.hydra_mobile.ui.viewmodel.WorkFlowViewModel
import android.view.ViewGroup
import android.widget.*
import ihsinformatics.com.hydra_mobile.R
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.utils.GlobalPreferences


class SelectWorkFlow : AppCompatActivity() {

    lateinit var workflows: List<WorkFlow>
    var selectedWorkflow: WorkFlow? = null

    lateinit var tvNext: TextView
    lateinit var workflowLayout: LinearLayout

    var scale: Float = 0f
    var dpAsPixels: Int = 0
    lateinit var params: LinearLayout.LayoutParams


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_workflow)

        initViews()


        val workflowViewModel = ViewModelProviders.of(this).get(WorkFlowViewModel::class.java)
        workflowViewModel.getAllWorkflowLiveData()
            .observe(this, Observer<List<WorkFlow>> { worklist ->
                if (worklist.isNotEmpty()) {

                    workflows = ArrayList<WorkFlow>(worklist)

                    workflowLayout.removeAllViews()

                    for (element in worklist) {
                        setWorkFlows(element)
                    }
                    setMarginBottomForLastTextView()

                } else {
                    ToastyWidget.getInstance()
                        .displayError(this, getString(R.string.need_to_sync), Toast.LENGTH_LONG)
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            })


        tvNext.setOnClickListener(View.OnClickListener {

            if (null!=selectedWorkflow) {

                val returnIntent = Intent()
                returnIntent.putExtra("result", selectedWorkflow!!.name)

                GlobalPreferences.getinstance(this)
                    .addOrUpdatePreference(GlobalPreferences.KEY.WORKFLOWUUID, selectedWorkflow!!.uuid)
                Global.WORKFLOWUUID = selectedWorkflow!!.uuid

                setResult(Activity.RESULT_OK, returnIntent)
                finish()

            } else {
                ToastyWidget.getInstance()
                    .displayError(this, "Please Select a Workflow", Toast.LENGTH_SHORT)
            }

        })

    }

    // We need to give margin bottom programatically to last textView of workflow because we used frame layout for
    // overlapping of next button over scrollview
    private fun setMarginBottomForLastTextView() {

        if(workflows.size>0)
        {
            var textView = workflowLayout.getChildAt(workflows.size-1) as TextView
            params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(40, 30, 40, 150)
            textView.setLayoutParams(params)

        }

    }

    private fun setWorkFlows(element: WorkFlow) {
        val textView = TextView(this)

        textView.setLayoutParams(params)

        textView.text = element.name
        textView.textSize = 20f
        textView.id = element.workflowId
        textView.tag = element.workflowId
        textView.setPadding(dpAsPixels, dpAsPixels, dpAsPixels, dpAsPixels)
        textView.setTextColor(resources.getColor(R.color.Black))
        textView.setBackground(getDrawable(R.drawable.white_circular_background))

        textView.setOnClickListener {

            if(null!=selectedWorkflow) {
                val previousSelection = workflowLayout.findViewById(selectedWorkflow!!.workflowId) as TextView
                previousSelection!!.setTextColor(resources.getColor(R.color.Black))
                previousSelection!!.setBackground(getDrawable(R.drawable.white_circular_background))
            }

            selectedWorkflow = element

            textView.setTextColor(resources.getColor(R.color.White))
            textView.setBackground(getDrawable(R.drawable.circular_background_next_button))

        }

        workflowLayout.addView(textView)
    }

    private fun initViews() {
        tvNext = findViewById(R.id.next)
        workflowLayout = findViewById(R.id.workflowLayout)

        // doing this inorder to set textView for workflows programatically
        scale = resources.displayMetrics.density
        dpAsPixels = (15 * scale + 0.5f).toInt()
        params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(40, 30, 40, 0)

    }


    override fun onBackPressed() {
    }
}

