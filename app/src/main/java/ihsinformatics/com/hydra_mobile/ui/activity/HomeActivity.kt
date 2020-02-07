package ihsinformatics.com.hydra_mobile.ui.activity


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.ihsinformatics.dynamicformsgenerator.PatientInfoFetcher
import com.ihsinformatics.dynamicformsgenerator.common.Constants
import com.ihsinformatics.dynamicformsgenerator.common.FormDetails
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames
import com.ihsinformatics.dynamicformsgenerator.network.pojos.PatientData
import com.ihsinformatics.dynamicformsgenerator.screens.Form
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import com.luseen.spacenavigation.SpaceItem
import com.luseen.spacenavigation.SpaceNavigationView
import com.luseen.spacenavigation.SpaceOnClickListener
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.FormSubmissionReqBody
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.remote.model.formSubmission
import ihsinformatics.com.hydra_mobile.data.remote.service.FormSubmissionApiService
import ihsinformatics.com.hydra_mobile.data.services.manager.MetaDataHelper
import ihsinformatics.com.hydra_mobile.databinding.ActivityHomeBinding
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.CommonLabActivity
import ihsinformatics.com.hydra_mobile.ui.adapter.DynamicFragmentAdapter
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import ihsinformatics.com.hydra_mobile.ui.viewmodel.FormViewModel
import ihsinformatics.com.hydra_mobile.ui.viewmodel.WorkflowPhasesMapViewModel
import ihsinformatics.com.hydra_mobile.utils.GlobalPreferences
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.PeriodType
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var spaceNavigationView: SpaceNavigationView
    private lateinit var toolbar: Toolbar
    lateinit var binding: ActivityHomeBinding
    lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    // val workflowPhasesRepository = WorkflowPhasesRepository(HydraApp.context!!)
    private lateinit var myDialog: Dialog
    private lateinit var mDynamicFragmentAdapter: DynamicFragmentAdapter

    private var llPatientInfoDisplayer: RelativeLayout? = null
    private var tvPatientName: TextView? = null
    private var tvAge: TextView? = null
    private var tvPatientLastName: TextView? = null
    private var tvAgeLabel: TextView? = null
    private var tvPatientIdentifier: TextView? = null
    private var ivGender: ImageView? = null

    private var Create_Patient_Count: Int = 0
    private var Send_Create_Patient_Count: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*screen level initialization*/

        Global.USERNAME = GlobalPreferences.getinstance(this).findPrferenceValue(GlobalPreferences.KEY.USERNAME, null)
        Global.PASSWORD = GlobalPreferences.getinstance(this).findPrferenceValue(GlobalPreferences.KEY.PASSWORD, null)
        Global.PROVIDER = GlobalPreferences.getinstance(this).findPrferenceValue(GlobalPreferences.KEY.PROVIDER, null)



        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        title = ""
        toolbar = binding.mainMenuLayout.toolbar
        setSupportActionBar(toolbar)
        initNavigationDrawer()
        initToolbar()
        bottomNav(savedInstanceState)

        llPatientInfoDisplayer = findViewById<RelativeLayout>(R.id.llPatientInfoDisplay)
        tvPatientName = findViewById<TextView>(R.id.tvName)
        tvPatientLastName = findViewById<TextView>(R.id.tvLastName)
        tvAge = findViewById<TextView>(R.id.tvAge)
        tvAgeLabel = findViewById<TextView>(R.id.tvAgeLabel)
        tvPatientIdentifier = findViewById<TextView>(R.id.tvId)
        ivGender = findViewById<ImageView>(R.id.ivGender)

        initSystemSettings()
        fillPatientInfoBar()

        /*user level initialization*/

        //session initialization
        if (sessionManager.isFirstTime()) {
            sessionManager.checkFirstTimeInstall(false)
        }


        //previous workflow check (if set then continue and fetch phases for that workflow)
        var selectedWorkFlow = GlobalPreferences.getinstance(this).findPrferenceValue(GlobalPreferences.KEY.WORKFLOWUUID, null)



        if (selectedWorkFlow == null) {
            startActivityForResult(Intent(this, SelectWorkFlow::class.java), 0)
        } else {
            getWorkFlowsAndBindAlongWithPhases(selectedWorkFlow)
        }


    }


    /*  Initialization functions   */

    private fun bottomNav(savedInstanceState: Bundle?) {

        spaceNavigationView = findViewById<SpaceNavigationView>(R.id.space)
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(SpaceItem(resources.getString(R.string.common_lab), R.drawable.ic_testtubes))
        spaceNavigationView.addSpaceItem(SpaceItem(resources.getString(R.string.history), R.drawable.ic_report_filled))



        spaceNavigationView.setSpaceOnClickListener(object : SpaceOnClickListener {

            override fun onCentreButtonClick() {


                if (Global.patientData == null) {
                    ToastyWidget.getInstance().displayWarning(this@HomeActivity, getString(R.string.patient_not_loaded), Toast.LENGTH_SHORT)
                } else {
                    startActivity(Intent(applicationContext, ProfileActivity::class.java))
                    finish()
                }


            }

            override fun onItemReselected(itemIndex: Int, itemName: String?) {
                if (itemName != null) {
                    bottomBarListener(itemName)
                }
            }

            override fun onItemClick(itemIndex: Int, itemName: String?) {
                if (itemName != null) {
                    bottomBarListener(itemName)
                }
            }
        })

    }

    fun bottomBarListener(itemName: String) {
        when (itemName) {

            resources.getString(R.string.common_lab) -> {

                if (true) {
                    ToastyWidget.getInstance().displaySuccess(this, "Coming Soon", Toast.LENGTH_LONG)
                } else {
                    if (!sessionManager.isOfflineMode()) {

                        if (isInternetConnected()) {
                            if (Global.patientData == null) {
                                ToastyWidget.getInstance().displayWarning(this@HomeActivity, getString(R.string.patient_not_loaded), Toast.LENGTH_SHORT)
                            } else {
                                startActivity(Intent(applicationContext, CommonLabActivity::class.java))
                                finish()
                            }
                        } else {
                            ToastyWidget.getInstance().displayWarning(this@HomeActivity, getString(R.string.internet_issue), Toast.LENGTH_SHORT)
                        }
                    } else {
                        ToastyWidget.getInstance().displayWarning(this@HomeActivity, getString(R.string.feature_of_offlineMode), Toast.LENGTH_LONG)

                    }
                }
            }

            resources.getString(R.string.history) -> {

                if (!sessionManager.isOfflineMode()) {
                    if (isInternetConnected()) {

                        if (Global.patientData == null) {
                            ToastyWidget.getInstance().displayWarning(this@HomeActivity, getString(R.string.patient_not_loaded), Toast.LENGTH_SHORT)
                        } else {
                            startActivity(Intent(applicationContext, ReportActivity::class.java))
                            finish()
                        }

                    } else {
                        ToastyWidget.getInstance().displayWarning(this@HomeActivity, getString(R.string.internet_issue), Toast.LENGTH_SHORT)

                    }

                } else {
                    ToastyWidget.getInstance().displayWarning(this@HomeActivity, getString(R.string.feature_of_offlineMode), Toast.LENGTH_LONG)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    private fun initToolbar() {
//        toolbar.action_notification_.setOnClickListener(View.OnClickListener {
//            startActivity(Intent(this, NotificationActivity::class.java))
//            finish()
//        })


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_more_options, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_logout) {

            logoutDialog()

        } else if (id == R.id.action_change_workflow) {
            startActivityForResult(Intent(this@HomeActivity, SelectWorkFlow::class.java), 0)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initNavigationDrawer() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        var drawerArrow = DrawerArrowDrawable(this)
        drawerArrow.color = ContextCompat.getColor(this@HomeActivity, R.color.colorWhite)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.isDrawerIndicatorEnabled = true
        toggle.drawerArrowDrawable = drawerArrow!!
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
        initPhase()
    }

    override fun onBackPressed() {

        drawerLayout = binding.drawerLayout
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {

            openDialog()
            //super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//

            R.id.nav_createPatient -> {
                Form.setENCOUNTER_NAME(ParamNames.ENCOUNTER_TYPE_CREATE_PATIENT)
                startActivity(Intent(this, Form::class.java))
            }
//            R.id.nav_faq -> {
//                startActivity(Intent(this, HelpActivity::class.java))
//                finish()
//            }
            R.id.nav_backup -> {

                Create_Patient_Count = 0
                Send_Create_Patient_Count = 0
                val saveableForms = DataAccess.getInstance().getAllForms(this)

                // find out how many create patients forms are in local db
                for (i in saveableForms) {
                    if (isInternetConnected()) {
                        if (i.encounterType.equals(ParamNames.ENCOUNTER_TYPE_CREATE_PATIENT)) {
                            Create_Patient_Count++
                        }
                    }
                }
                //sends create patient form first form
                if (Create_Patient_Count != 0) {
                    for (i in saveableForms) {
                        if (isInternetConnected()) {
                            if (i.encounterType.equals(ParamNames.ENCOUNTER_TYPE_CREATE_PATIENT)) {
                                sendData(i.formData, i.formId)
                            }
                        } else {
                            ToastyWidget.getInstance().displayError(this, getString(R.string.internet_issue), Toast.LENGTH_SHORT)
                        }
                    }
                } else {
                    for (i in saveableForms) {
                        if (isInternetConnected()) {
                            sendData(i.formData, i.formId)

                        } else {
                            ToastyWidget.getInstance().displayError(this, getString(R.string.internet_issue), Toast.LENGTH_SHORT)
                        }
                    }
                }
            }


            R.id.nav_sync -> {

                if (isInternetConnected()) {
                    openMetaDataFetcher()
                } else {
                    ToastyWidget.getInstance().displayError(this@HomeActivity, getString(R.string.internet_issue), Toast.LENGTH_SHORT)

                }
            }
            R.id.nav_search -> {

                if (isInternetConnected()) {
                    startActivityForResult(Intent(this, SearchActivity::class.java), 112)
                    finish()
                } else {
                    ToastyWidget.getInstance().displayWarning(this@HomeActivity, getString(R.string.internet_issue), Toast.LENGTH_SHORT)
                }


            }

            R.id.nav_offline_search -> {

                PatientInfoFetcher.init(Constant.formName, PatientInfoFetcher.REQUEST_TYPE.FETCH_INFO)
                startActivityForResult(Intent(this, PatientInfoFetcher::class.java), 112)
            }

            R.id.nav_logout -> {
                logoutDialog()

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun sendData(formData: JSONObject?, formId: Long) {

        val dataSender = RequestManager(applicationContext, sessionManager.getUsername(), sessionManager.getPassword()).getFormRetrofit().create(FormSubmissionApiService::class.java)
        val dataArray = formData!!.getJSONArray(ParamNames.DATA).toString()
        val metadata = formData!!.getJSONObject(ParamNames.METADATA).toString()

        val requestBody = FormSubmissionReqBody(dataArray, metadata)
        //val body: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBody)

        dataSender.submitForm(requestBody).enqueue(object : Callback<formSubmission> {
            override fun onFailure(call: Call<formSubmission>, t: Throwable) {

                ToastyWidget.getInstance().displayError(this@HomeActivity, "Error", Toast.LENGTH_SHORT)
            }

            override fun onResponse(
                call: Call<formSubmission>, response: Response<formSubmission>
                                   ) {
                if (response.isSuccessful) {
                    ToastyWidget.getInstance().displaySuccess(this@HomeActivity, "Success", Toast.LENGTH_SHORT)
                    DataAccess.getInstance().deleteFormByFormID(this@HomeActivity, formId)
                    Send_Create_Patient_Count++
                    if (Create_Patient_Count == Send_Create_Patient_Count) {
                        val saveableForms = DataAccess.getInstance().getAllForms(this@HomeActivity)

                        for (i in saveableForms) {
                            if (isInternetConnected()) {
                                sendData(i.formData, i.formId)
                            } else {
                                ToastyWidget.getInstance().displayError(this@HomeActivity, getString(R.string.internet_issue), Toast.LENGTH_SHORT)
                            }
                        }
                    }
                } else {
                    ToastyWidget.getInstance().displayError(this@HomeActivity, "Server error", Toast.LENGTH_SHORT)

                }
            }

        })
    }

    private fun initPhase() {
        binding.mainMenuLayout.vpPhases.offscreenPageLimit = 2
        binding.mainMenuLayout.vpPhases.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.mainMenuLayout.tbPhase))
        binding.mainMenuLayout.tbPhase.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.mainMenuLayout.vpPhases.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

    }


/*  My Logic Functions  */

/*   private fun openLoadingScreen() {

       val fragmentManager = supportFragmentManager.beginTransaction()
       val loadingScreening = LoadingProgressDialog.newInstance()
       loadingScreening.show(fragmentManager, "loading screening")


       Handler().postDelayed(
           Runnable {
               kotlin.run {
                   sessionManager.checkFirstTimeInstall(false)
                   loadingScreening.dismiss()
                   getWorkFlowsAndBindAlongWithPhases()
               }
           }, 5000
       )
   }
*/


    private fun fillPatientInfoBar() {

        if (Global.patientData != null) {
            Global.temp = Global.patientData
            tvPatientName?.visibility = View.VISIBLE
            tvPatientLastName?.visibility = View.VISIBLE
            tvAge?.visibility = View.VISIBLE
            tvAgeLabel?.visibility = View.VISIBLE
            var identifiers = ""
            val ids = Global.patientData.getIdentifiers()
            if (ids != null) {
                val it = ids!!.keys.iterator()
                while (it.hasNext()) {
                    val key = it.next()
                    val value = ids!!.get(key)
                    identifiers += value/*+", "*/
                }
            }
            tvPatientName?.setText(Global.patientData.getPatient().getGivenName().toUpperCase())
            tvPatientLastName?.setText(Global.patientData.getPatient().getFamilyName().toUpperCase())
            // tvAge.setText(patientData.getPatient().getAge() + ""); //TODO get dob and display full age till days
            val birthDate = Global.patientData.getPatient().getBirthDate()
            val birthTime = DateTime(birthDate)
            val nowTime = DateTime()


            val interval = Interval(birthTime, nowTime)
            val period = interval.toPeriod().normalizedStandard(PeriodType.yearMonthDay())
            val years = period.getYears()
            val months = period.getMonths()
            val days = period.getDays()
            tvAge?.setText(years.toString() + " years, " + months.toString() + " months, " + days.toString() + " days")
            Global.patientData.getPatient().age = years
            tvPatientIdentifier?.setText(identifiers)
            if (Global.patientData.getPatient().getGender().toLowerCase().startsWith("m")) {
                ivGender?.setImageDrawable(getDrawable(com.ihsinformatics.dynamicformsgenerator.R.drawable.ic_user_profile))
            } else {
                ivGender?.setImageDrawable(getDrawable(com.ihsinformatics.dynamicformsgenerator.R.drawable.ic_user_female))
            }
        } else {
            tvPatientName?.visibility = View.GONE
            tvPatientLastName?.visibility = View.GONE
            tvAge?.visibility = View.GONE
            tvAgeLabel?.visibility = View.GONE
            tvPatientIdentifier?.setText("Patient Not Loaded")
            ivGender?.setImageDrawable(getDrawable(com.ihsinformatics.dynamicformsgenerator.R.drawable.ic_user_profile))

        }
    }


    //Todo workflow must come according to ID and not name
    fun getWorkFlowsAndBindAlongWithPhases(selectedWorkFlow: String) {


        val workflowPhaseMapViewModel = ViewModelProviders.of(this).get(WorkflowPhasesMapViewModel::class.java)

        if (null != selectedWorkFlow && !selectedWorkFlow.equals("")) {
            val work = workflowPhaseMapViewModel.getPhasesByWorkFlowUUID(selectedWorkFlow)


            //TODO remove repository and try viewModel
            // work = workflowPhasesRepository.getPhasesByWorkFlowName(selectedWorkFlow)
            binding.mainMenuLayout.tbPhase.removeAllTabs()
            binding.mainMenuLayout.vpPhases.removeAllViews()
            if (work.isNotEmpty()) {

                for (element in work) {
                    binding.mainMenuLayout.tbPhase.addTab(binding.mainMenuLayout.tbPhase.newTab().setText("" + element.phaseName))
                }


                mDynamicFragmentAdapter = DynamicFragmentAdapter(supportFragmentManager, binding.mainMenuLayout.tbPhase.tabCount, work)
                binding.mainMenuLayout.vpPhases.adapter = mDynamicFragmentAdapter
                binding.mainMenuLayout.vpPhases.currentItem = 0


            }
        } else {
            ToastyWidget.getInstance().displayError(this@HomeActivity, "Workflow not loaded", Toast.LENGTH_SHORT)
        }


    }


/*Return from Select Workflow Activity Logic*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode === 0) {
            if (resultCode === Activity.RESULT_OK) {
                val result = data?.getStringExtra("result")
                GlobalPreferences.getinstance(this).addOrUpdatePreference(GlobalPreferences.KEY.WORKFLOW, result)
                if (result != null) {
                    val selectedWorkFlow = GlobalPreferences.getinstance(this).findPrferenceValue(GlobalPreferences.KEY.WORKFLOWUUID, "")

                    getWorkFlowsAndBindAlongWithPhases(selectedWorkFlow)
                }
                Toast.makeText(this, "Workflow Changed to: " + result, Toast.LENGTH_SHORT).show()
            }
            if (resultCode === Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode === 112) {
            fillPatientInfoBar()
        }
    }


    private fun logoutDialog() {
        val dialog = AlertDialog.Builder(this).setMessage("Are you sure to logout?").setTitle("Are you sure?").setNegativeButton("No", null).setPositiveButton("Yes") { dialog, which ->

            SessionManager(applicationContext).logoutUser()
            GlobalPreferences.getinstance(this).addOrUpdatePreference(GlobalPreferences.KEY.WORKFLOW, null)
            finish()

        }
        dialog.show()
    }


    override fun onResume() {
        super.onResume()
        loadFormDataInEncounterTypes()
    }

    private fun loadFormDataInEncounterTypes() {
        val formViewModel = ViewModelProviders.of(this).get(FormViewModel::class.java)

        val forms = formViewModel.getAllForms()
        Constants.clearEncounters()
        for (i in forms) {

            Constants.setEncounterType(i.id, i.name)
            Constants.setEncounterTypeData(i.name, i.questions)
            Constants.setFormDetails(i.id, FormDetails(i.componentFormId, i.componentFormUUID))
        }

    }


    private fun openMetaDataFetcher() {
        val metaDataHelper = MetaDataHelper(this)
        metaDataHelper.getAllMetaData(object : RESTCallback {
            override fun <T> onSuccess(o: T) {
                val isSuccess = o as Boolean
                if (isSuccess) {

                    ToastyWidget.getInstance().displaySuccess(this@HomeActivity, getString(R.string.sync_success), Toast.LENGTH_LONG)

                }

            }

            override fun onFailure(t: Throwable) {
                ToastyWidget.getInstance().displayError(this@HomeActivity, getString(R.string.sync_error), Toast.LENGTH_LONG)

            }
        })


    }


    fun initSystemSettings() {
        val identifierFormat = DataAccess.getInstance().fetchSystemSettingsByUUID(this, "9b68a10b-3ede-43f6-b019-d0823e28ebd1")  //UUID for hydra.IdentifierFormat
        val dateFormat = DataAccess.getInstance().fetchSystemSettingsByUUID(this, "6a78a10b-3eae-43f6-b019-d0823e28ebd1")  //UUID for hydra.dateFormat

        Global.identifierFormat = identifierFormat.value
        Global.setDateFormat(dateFormat.value)

    }


    @Throws(JSONException::class) private fun startForm(patientData: PatientData, bundle: Bundle) {
        var bundle: Bundle? = bundle
        if (bundle == null) bundle = Bundle()
        bundle.putSerializable(ParamNames.DATA, patientData)
        val intent = Intent(this, Form::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

}