package ihsinformatics.com.hydra_mobile.ui.activity.labModule

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.repository.LabTestTypeRepository
import ihsinformatics.com.hydra_mobile.data.repository.WorkflowPhasesRepository
import ihsinformatics.com.hydra_mobile.ui.adapter.CustomExpandableTestAdderAdapter
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_profile.*


class TestAdder : BaseActivity() {

    internal var expandableListView: ExpandableListView? = null
    internal var adapter: ExpandableListAdapter? = null
    internal var titleList: List<String>? = null


    val data: HashMap<String, ArrayList<String>>
        get() {

            val listData = HashMap<String, ArrayList<String>>()


            var labTestRepository = LabTestTypeRepository(this@TestAdder)

            var allTestTypes = labTestRepository.getAllTestTypes()

            for (i in 0 until allTestTypes.size) {
                if (!listData.containsKey(allTestTypes.get(i).testGroup)) {
                    val list: ArrayList<String> = ArrayList<String>()
                    list.add(allTestTypes.get(i).name)
                    listData.put(allTestTypes.get(i).testGroup, list)
                } else {
                    listData.get(allTestTypes.get(i).testGroup)!!.add(allTestTypes.get(i).name)
                }

            }

            return listData;
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_adder)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        expandableListView = findViewById<ExpandableListView>(R.id.expandableListView)

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val width = metrics.widthPixels

        expandableListView!!.setIndicatorBounds(
            width - GetPixelFromDips(50f),
            width - GetPixelFromDips(10f)
        );




        if (expandableListView != null) {
            val listData = data
            titleList = ArrayList(listData.keys)
            adapter =
                CustomExpandableTestAdderAdapter(
                    this,
                    titleList as ArrayList<String>,
                    listData
                )
            expandableListView!!.setAdapter(adapter)
        }

        init();

    }

    fun GetPixelFromDips(pixels: Float): Int {
        // Get the screen's density scale
        val scale = resources.displayMetrics.density
        // Convert the dps to pixels, based on density scale
        return (pixels * scale + 0.5f).toInt()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, CommonLabActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {

        startActivity(Intent(this, CommonLabActivity::class.java))
        finish()
        super.onBackPressed()
    }


    private fun init() {
//        val phasesComponentJoinViewModel = ViewModelProviders.of(this).get(
//            PhaseComponentJoinViewModel::class.java)
//
//        phasesComponentJoinViewModel.getFormsByPhaseUUID()

    }
}
