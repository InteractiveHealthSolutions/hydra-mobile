package ihsinformatics.com.hydra_mobile.ui.activity.labModule

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.CommonLabApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.Encounter
import ihsinformatics.com.hydra_mobile.data.remote.service.CommonLabApiService
import ihsinformatics.com.hydra_mobile.data.repository.LabTestTypeRepository
import ihsinformatics.com.hydra_mobile.ui.adapter.CustomExpandableTestAdderAdapter
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import ihsinformatics.com.hydra_mobile.ui.viewmodel.WorkflowPhasesMapViewModel
import ihsinformatics.com.hydra_mobile.utils.GlobalPreferences
import kotlinx.android.synthetic.main.activity_profile.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber


class TestAdder : BaseActivity() {

    internal var expandableListView: ExpandableListView? = null
    var adapter: CustomExpandableTestAdderAdapter? = null
    internal var titleList: List<String>? = null


    lateinit var phasesSpinner: Spinner
    lateinit var encounterSpinner: Spinner

    var testTypeMap = HashMap<String, String>()

    var labTestTypeUUIDs = ArrayList<String>()


    lateinit var sendParams: JSONObject
    lateinit var order: JSONObject


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
                testTypeMap.put(allTestTypes.get(i).name, allTestTypes.get(i).uuid)
            }

            return listData;
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_adder)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        //TODO HardCODED most of the fields... ~Taha
        var patientUUID: String = "dbac89bb-508b-4693-aad1-3b5a5310252e"
        var conceptUUID: String = "17AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        var ordererUUID: String = sessionManager.getProviderUUID()
        var type: String = "testorder"
        var careSettingUUID: String = "6f0c9a92-6f24-11e3-af88-005056821db0"
        var encounterUUID: String = "9fd970e7-0289-490d-bd37-8b0bb6178158"
        lateinit var labTestType: String
        var labReferenceNumber: String = "adding test test2"
        var labInstructions: String = ""

        expandableListView = findViewById<ExpandableListView>(R.id.expandableListView)
        phasesSpinner = findViewById<Spinner>(R.id.phasesSpinner)
        encounterSpinner = findViewById<Spinner>(R.id.encounterSpinner)


        val intent = intent
        val jsonObj = intent.getStringExtra("encountersList")

        val gson = Gson()
        val token: TypeToken<ArrayList<Encounter?>?> = object : TypeToken<ArrayList<Encounter?>?>() {}
        var encountersList: ArrayList<Encounter> = gson.fromJson(jsonObj, token.type)


        var encountersMap = HashMap<String, String>()
        for (i in 0 until encountersList.size) {
            encountersMap.put(encountersList.get(i).display, encountersList.get(i).uuid)
        }
        encounterSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, encountersMap.keys.toList())


        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val width = metrics.widthPixels

        expandableListView!!.setIndicatorBounds(width - GetPixelFromDips(50f), width - GetPixelFromDips(10f));




        if (expandableListView != null) {
            val listData = data
            titleList = ArrayList(listData.keys)
            adapter = CustomExpandableTestAdderAdapter(this, titleList as ArrayList<String>, listData)
            expandableListView!!.setAdapter(adapter)


        }


        val saveRequest = findViewById<Button>(R.id.saveRequest)

        saveRequest.setOnClickListener {
            labTestTypeUUIDs.clear()

            var addTestOrder = RequestManager(applicationContext, sessionManager.getUsername(), sessionManager.getPassword()).getPatientRetrofit().create(CommonLabApiService::class.java)

            var myMutable = adapter?.getCheckBoxData()

            if (myMutable != null) {
                for (i in myMutable) {
                    labTestTypeUUIDs.add(testTypeMap.get(i.key).toString())
                }
            }


            for (i in encountersList) {
                if (i.display.equals(encounterSpinner.selectedItem.toString())) {
                    encounterUUID = i.uuid
                    //Todo need to add other required paramters  ~Taha
                    break
                }
            }

            for (i in labTestTypeUUIDs) {
                order = JSONObject()
                sendParams = JSONObject()
                labTestType = i
                order.put("patient", patientUUID)
                order.put("concept", conceptUUID)
                order.put("orderer", ordererUUID)
                order.put("type", type)
                order.put("careSetting", careSettingUUID)
                order.put("encounter", encounterUUID)

                sendParams.put("order", order)
                sendParams.put("labTestType", labTestType)
                sendParams.put("labReferenceNumber", labReferenceNumber)
                sendParams.put("labInstructions", labInstructions)

                val body: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), sendParams.toString())
                addTestOrder.addLabTestOrder(body).enqueue(object : Callback<CommonLabApiResponse> {
                    override fun onResponse(
                        call: Call<CommonLabApiResponse>, response: Response<CommonLabApiResponse>
                                           ) {
                        Timber.e(response.message())
                        if (response.isSuccessful) {

                            ToastyWidget.getInstance().displaySuccess(this@TestAdder, "Test Order Added Successfully", Toast.LENGTH_LONG)
                            startActivity(Intent(this@TestAdder, CommonLabActivity::class.java))

                        } else {

                            ToastyWidget.getInstance().displaySuccess(this@TestAdder, "Error Adding Test Order", Toast.LENGTH_LONG)

                        }
                    }

                    override fun onFailure(call: Call<CommonLabApiResponse>, t: Throwable) {
                        Timber.e(t.localizedMessage)
                        ToastyWidget.getInstance().displaySuccess(this@TestAdder, getString(R.string.internet_issue), Toast.LENGTH_LONG)

                    }

                })

            }
        }


        init()

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

        var selectedWorkFlow = GlobalPreferences.getinstance(this@TestAdder).findPrferenceValue(GlobalPreferences.KEY.WORKFLOWUUID, "-1")


        // if(!selectedWorkFlow.equals("-1")) {
        val workflowPhaseMapViewModel = ViewModelProviders.of(this).get(WorkflowPhasesMapViewModel::class.java)
        val work = workflowPhaseMapViewModel.getPhasesByWorkFlowUUID(selectedWorkFlow)

        var phasesArraylist = HashMap<String, String>()
        for (i in 0 until work.size) {
            phasesArraylist.put(work.get(i).phaseName, work.get(i).phaseUUID)
        }

        phasesSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, phasesArraylist.keys.toList())

//            phasesArraylist.get(phasesSpinner.selectedItem.toString())

//        }else
//        {
//            Toast.makeText(this,"No Phases to Select",Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this,HomeActivity::class.java))  //IF no phases then sending to home page
//        }

    }
}