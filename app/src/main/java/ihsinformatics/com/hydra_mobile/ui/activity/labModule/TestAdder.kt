package ihsinformatics.com.hydra_mobile.ui.activity.labModule

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.Encounter
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.TestSample
import ihsinformatics.com.hydra_mobile.data.repository.LabTestTypeRepository
import ihsinformatics.com.hydra_mobile.data.repository.WorkflowPhasesRepository
import ihsinformatics.com.hydra_mobile.ui.activity.HomeActivity
import ihsinformatics.com.hydra_mobile.ui.adapter.CustomExpandableTestAdderAdapter
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import ihsinformatics.com.hydra_mobile.ui.viewmodel.PhaseComponentJoinViewModel
import ihsinformatics.com.hydra_mobile.ui.viewmodel.WorkflowPhasesMapViewModel
import ihsinformatics.com.hydra_mobile.utils.GlobalPreferences
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.runBlocking


class TestAdder : BaseActivity() {

    internal var expandableListView: ExpandableListView? = null
    internal var adapter: ExpandableListAdapter? = null
    internal var titleList: List<String>? = null

    lateinit var phasesSpinner: Spinner
    lateinit var encounterSpinner: Spinner


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
        phasesSpinner = findViewById<Spinner>(R.id.phasesSpinner)
        encounterSpinner = findViewById<Spinner>(R.id.encounterSpinner)


        val intent = intent
        val jsonObj = intent.getStringExtra("encountersList")

        val gson = Gson()
        val token: TypeToken<ArrayList<Encounter?>?> =
            object : TypeToken<ArrayList<Encounter?>?>() {}
        var encountersList: ArrayList<Encounter> = gson.fromJson(jsonObj, token.type)



        var encountersMap=HashMap<String,String>()
        for(i in 0 until encountersList.size)
        {
            encountersMap.put(encountersList.get(i).display,encountersList.get(i).uuid)
        }
        encounterSpinner.adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,encountersMap.keys.toList())


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

        var selectedWorkFlow= GlobalPreferences.getinstance(this@TestAdder).findPrferenceValue(
            GlobalPreferences.KEY.WORKFLOWUUID,"-1")


       // if(!selectedWorkFlow.equals("-1")) {
            val workflowPhaseMapViewModel =
                ViewModelProviders.of(this).get(WorkflowPhasesMapViewModel::class.java)
            val work = workflowPhaseMapViewModel.getPhasesByWorkFlowUUID(selectedWorkFlow)

            var phasesArraylist=HashMap<String,String>()
            for(i in 0 until work.size)
            {
                phasesArraylist.put(work.get(i).phaseName,work.get(i).phaseUUID)
            }

            phasesSpinner.adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,phasesArraylist.keys.toList())

//            phasesArraylist.get(phasesSpinner.selectedItem.toString())

//        }else
//        {
//            Toast.makeText(this,"No Phases to Select",Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this,HomeActivity::class.java))  //IF no phases then sending to home page
//        }

    }
}