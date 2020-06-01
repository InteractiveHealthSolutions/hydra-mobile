package ihsinformatics.com.hydra_mobile.ui.activity


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.*
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
import com.google.firebase.analytics.FirebaseAnalytics
import com.ihsinformatics.dynamicformsgenerator.common.Constants
import com.ihsinformatics.dynamicformsgenerator.common.FormDetails
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess
import com.ihsinformatics.dynamicformsgenerator.data.database.SaveableForm
import com.ihsinformatics.dynamicformsgenerator.network.ParamNames
import com.ihsinformatics.dynamicformsgenerator.network.pojos.PatientData
import com.ihsinformatics.dynamicformsgenerator.screens.Form
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import com.luseen.spacenavigation.SpaceItem
import com.luseen.spacenavigation.SpaceNavigationView
import com.luseen.spacenavigation.SpaceOnClickListener
import ihsinformatics.com.hydra_mobile.BuildConfig
import ihsinformatics.com.hydra_mobile.R
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
import ihsinformatics.com.hydra_mobile.ui.dialogs.DisplaySettingsDialogFragment
import ihsinformatics.com.hydra_mobile.ui.dialogs.NetworkProgressDialog
import ihsinformatics.com.hydra_mobile.ui.viewmodel.FormViewModel
import ihsinformatics.com.hydra_mobile.ui.viewmodel.WorkflowPhasesMapViewModel
import ihsinformatics.com.hydra_mobile.utils.GlobalPreferences
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import kotlinx.android.synthetic.main.nav_header_main_menu.view.*
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.PeriodType
import org.json.JSONException
import org.json.JSONObject
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var networkProgressDialog: NetworkProgressDialog

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

    private lateinit var covidInfo: LinearLayout
    private lateinit var covidAlert: LinearLayout
    private lateinit var damage: TextView
    private lateinit var dropDownIcon: ImageView

    private lateinit var covidInfoImage: ImageView
    private lateinit var covidInfoLayout: LinearLayout
    private lateinit var topLayout: LinearLayout


    private var sentCreatePatientsCount: Int = 0
    private var sentEncountersCount: Int = 0
    private var isUp = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        /*screen level initialization*/


        Global.USERUUID = GlobalPreferences.getinstance(this)
            .findPrferenceValue(GlobalPreferences.KEY.USERUUID, null)
        Global.USERNAME = GlobalPreferences.getinstance(this)
            .findPrferenceValue(GlobalPreferences.KEY.USERNAME, null)
        Global.PASSWORD = GlobalPreferences.getinstance(this)
            .findPrferenceValue(GlobalPreferences.KEY.PASSWORD, null)
        Global.PROVIDER = GlobalPreferences.getinstance(this)
            .findPrferenceValue(GlobalPreferences.KEY.PROVIDER, null)




        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        title = ""
        toolbar = binding.mainMenuLayout.toolbar
        setSupportActionBar(toolbar)
        initNavigationDrawer()
        initToolbar()
        bottomNav(savedInstanceState)

        networkProgressDialog = NetworkProgressDialog(this)
        llPatientInfoDisplayer = findViewById<RelativeLayout>(R.id.llPatientInfoDisplay)
        tvPatientName = findViewById<TextView>(R.id.tvName)
        tvPatientLastName = findViewById<TextView>(R.id.tvLastName)
        tvAge = findViewById<TextView>(R.id.tvAge)
        tvAgeLabel = findViewById<TextView>(R.id.tvAgeLabel)
        tvPatientIdentifier = findViewById<TextView>(R.id.tvId)
        ivGender = findViewById<ImageView>(R.id.ivGender)

        covidAlert = findViewById(R.id.covidAlert)
        covidInfo = findViewById(R.id.covidInfo)
        dropDownIcon = findViewById(R.id.dropDownIcon)

        damage = findViewById(R.id.damage)
        covidInfoLayout = findViewById(R.id.lungLayout)
        covidInfoImage = findViewById(R.id.lungs)

        topLayout = findViewById(R.id.topLayout)



        covidAlert.setOnClickListener {
            if (isUp) {
                covidInfo.animate().translationY(-covidInfo.height.toFloat()).alpha(0f);
                dropDownIcon.setImageDrawable(getDrawable(R.drawable.ic_alertmenu))
                //topLayout.visibility=View.GONE
            } else {
                covidInfo.animate().translationY(0f).alpha(1f)
                dropDownIcon.setImageDrawable(getDrawable(R.drawable.ic_arrows))
                //topLayout.visibility=View.VISIBLE
            }
            isUp = !isUp;

        }



        initSystemSettings()
        fillPatientInfoBar()
        setLanguage()

        /*user level initialization*/

        //session initialization
        if (sessionManager.isFirstTime()) {
            sessionManager.checkFirstTimeInstall(false)
        }


        //previous workflow check (if set then continue and fetch phases for that workflow)
        var selectedWorkFlow = GlobalPreferences.getinstance(this)
            .findPrferenceValue(GlobalPreferences.KEY.WORKFLOWUUID, null)
        Global.WORKFLOWUUID = GlobalPreferences.getinstance(this)
            .findPrferenceValue(GlobalPreferences.KEY.WORKFLOWUUID, null)
        Global.HYRDA_CURRENT_URL = GlobalPreferences.getinstance(this)
            .findPrferenceValue(GlobalPreferences.KEY.HYDRA_URL, null)



        if (!sessionManager.isCompleteDataDownloaded()) {
            startActivity(Intent(this, LoginActivity::class.java))
        } else if (selectedWorkFlow == null) {
            startActivityForResult(Intent(this, SelectWorkFlow::class.java), 0)
        } else if (Global.HYRDA_CURRENT_URL == null || Global.HYRDA_CURRENT_URL.trim().equals("")) {
            startActivity(Intent(this, LoginActivity::class.java))
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
                    ToastyWidget.getInstance()
                        .displayWarning(this@HomeActivity, getString(R.string.patient_not_loaded), Toast.LENGTH_SHORT)
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
                    ToastyWidget.getInstance()
                        .displaySuccess(this, "Coming Soon", Toast.LENGTH_LONG)
                } else {
                    if (!sessionManager.isOfflineMode()) {

                        if (isInternetConnected()) {
                            if (Global.patientData == null) {
                                ToastyWidget.getInstance()
                                    .displayWarning(this@HomeActivity, getString(R.string.patient_not_loaded), Toast.LENGTH_SHORT)
                            } else {
                                startActivity(Intent(applicationContext, CommonLabActivity::class.java))
                                finish()
                            }
                        } else {
                            ToastyWidget.getInstance()
                                .displayWarning(this@HomeActivity, getString(R.string.internet_issue), Toast.LENGTH_SHORT)
                        }
                    } else {
                        ToastyWidget.getInstance()
                            .displayWarning(this@HomeActivity, getString(R.string.feature_of_offlineMode), Toast.LENGTH_LONG)

                    }
                }
            }


            resources.getString(R.string.history) -> {

                if (Global.patientData == null) {
                    ToastyWidget.getInstance()
                        .displayWarning(this@HomeActivity, getString(R.string.patient_not_loaded), Toast.LENGTH_SHORT)
                } else {
                    startActivity(Intent(applicationContext, ReportActivity::class.java))
                    finish()
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
        } else if (id == R.id.settings) {
            openSettingDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initNavigationDrawer() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navView.getHeaderView(0).tvVersionNumber.setText("Version: " + BuildConfig.VERSION_NAME)
        navView.getHeaderView(0).tvLoggedInAs.setText("Logged in as: " + Global.USERNAME)
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

    private var createPatientForm: List<SaveableForm> = arrayListOf<SaveableForm>();
    private var encounterForms: List<SaveableForm> = arrayListOf<SaveableForm>();

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_createPatient -> {
                Form.setENCOUNTER_NAME(ParamNames.ENCOUNTER_TYPE_CREATE_PATIENT, ParamNames.ENCOUNTER_TYPE_CREATE_PATIENT)
                startActivityForResult(Intent(this, Form::class.java), 112)

            }
            R.id.nav_backup -> {
                startActivity(Intent(this, FormUpload::class.java))
            }


            R.id.nav_sync -> {

                if (isInternetConnected()) {
                    openMetaDataFetcher()
                } else {
                    ToastyWidget.getInstance()
                        .displayError(this@HomeActivity, getString(R.string.internet_issue), Toast.LENGTH_SHORT)

                }
            }

            R.id.nav_form_edit -> {
                startActivity(Intent(this, FormEdit::class.java))
            }

            R.id.import_data -> {
                val dialog = AlertDialog.Builder(this)
                    .setMessage(getString(R.string.are_you_sure_to_import))
                    .setTitle(getString(R.string.are_you_sure))
                    .setNegativeButton(getString(R.string.no), null)
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->

                        startActivityForResult(Intent(this, TermsAndConditions::class.java), 113)

                    }
                dialog.show()


            }

            R.id.export_data -> {
                val dialog = AlertDialog.Builder(this)
                    .setMessage(getString(R.string.are_you_sure_to_export))
                    .setTitle(getString(R.string.are_you_sure))
                    .setNegativeButton(getString(R.string.no), null)
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->


                        startActivityForResult(Intent(this, TermsAndConditions::class.java), 114)

                    }
                dialog.show()
            }

            R.id.userReports -> {
                startActivity(Intent(applicationContext, UserReports::class.java))
                finish()
            }



            R.id.nav_search -> {

                startActivityForResult(Intent(this, SearchActivity::class.java), 112)

            }

            R.id.nav_logout -> {

                logoutDialog()


            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
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


    private fun makeFolder() {
        val direct = File(Environment.getExternalStorageDirectory().toString() + "/BackupFolder")

        if (!direct.exists()) {
            if (direct.mkdir()) {
                //directory is created;
            }
        }
    }


    private fun importDB() {
        // TODO Auto-generated method stub
        try {
            val sd = Environment.getExternalStorageDirectory()
            val data = Environment.getDataDirectory()
            if (sd.canWrite()) {
                val currentDBPath = "//data//" + "ihsinformatics.com.hydra_mobile" + "//databases//" + "greendao_demo.db"
                val backupDBPath = "/BackupFolder/greendao_demo.db"
                val backupDB = File(data, currentDBPath)
                val currentDB = File(sd, backupDBPath)
                val src: FileChannel = FileInputStream(currentDB).getChannel()
                val dst: FileChannel = FileOutputStream(backupDB).getChannel()
                dst.transferFrom(src, 0, src.size())
                src.close()
                dst.close()
                Toast.makeText(baseContext, backupDB.toString(), Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(baseContext, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    //exporting database
    private fun exportDB() {
        // TODO Auto-generated method stub
        try {
            val sd = Environment.getExternalStorageDirectory()
            val data = Environment.getDataDirectory()
            if (sd.canWrite()) {
                val currentDBPath = "//data//" + "ihsinformatics.com.hydra_mobile" + "//databases//" + "greendao_demo.db"
                val backupDBPath = "/BackupFolder/greendao_demo.db"
                val currentDB = File(data, currentDBPath)
                val backupDB = File(sd, backupDBPath)
                val src = FileInputStream(currentDB).channel
                val dst = FileOutputStream(backupDB).channel
                dst.transferFrom(src, 0, src.size())
                src.close()
                dst.close()
                Toast.makeText(baseContext, backupDB.toString(), Toast.LENGTH_LONG).show()
            }
        } catch (e: java.lang.Exception) {
            Toast.makeText(baseContext, e.toString(), Toast.LENGTH_LONG).show()
        }
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

            setCovidResults()

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
            tvPatientLastName?.setText(Global.patientData.getPatient().getFamilyName()
                .toUpperCase())
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
            covidInfo.visibility = View.GONE
            covidAlert.visibility = View.GONE
            topLayout.visibility = View.GONE
            tvPatientName?.visibility = View.GONE
            tvPatientLastName?.visibility = View.GONE
            tvAge?.visibility = View.GONE
            tvAgeLabel?.visibility = View.GONE
            tvPatientIdentifier?.setText("Patient Not Loaded")
            ivGender?.setImageDrawable(getDrawable(com.ihsinformatics.dynamicformsgenerator.R.drawable.ic_user_profile))

        }
    }

    private fun setCovidResults() {
        damage.text = Global.patientData.covidResult
        covidAlert.visibility = View.VISIBLE
        topLayout.visibility = View.VISIBLE
        covidInfo.visibility = View.VISIBLE

        when (damage.text.toString().toUpperCase()) {
            "LOW" -> {
                covidInfoImage.setImageDrawable(getDrawable(R.drawable.ic_alert_low))
                covidInfoLayout.background = getDrawable(R.drawable.circular_background_alert_low)
            }
            "HIGH" -> {
                covidInfoImage.setImageDrawable(getDrawable(R.drawable.ic_alert))
                covidInfoLayout.background = getDrawable(R.drawable.circular_background_alert_high)
            }
            "MEDIUM" -> {
                covidInfoImage.setImageDrawable(getDrawable(R.drawable.ic_alert_med))
                covidInfoLayout.background = getDrawable(R.drawable.circular_background_alert_medium)
            }
            else -> {
                covidInfo.visibility = View.GONE
                covidAlert.visibility = View.GONE
                topLayout.visibility = View.GONE
            }
        }
    }


    //Todo workflow must come according to ID and not name
    fun getWorkFlowsAndBindAlongWithPhases(selectedWorkFlow: String) {


        val workflowPhaseMapViewModel = ViewModelProviders.of(this)
            .get(WorkflowPhasesMapViewModel::class.java)

        if (null != selectedWorkFlow && !selectedWorkFlow.equals("")) {
            val work = workflowPhaseMapViewModel.getPhasesByWorkFlowUUID(selectedWorkFlow)


            //TODO remove repository and try viewModel
            // work = workflowPhasesRepository.getPhasesByWorkFlowName(selectedWorkFlow)
            binding.mainMenuLayout.tbPhase.removeAllTabs()
            binding.mainMenuLayout.vpPhases.removeAllViews()
            if (work.isNotEmpty()) {

                for (element in work) {
                    binding.mainMenuLayout.tbPhase.addTab(binding.mainMenuLayout.tbPhase.newTab()
                        .setText("" + element.phaseName))
                }


                mDynamicFragmentAdapter = DynamicFragmentAdapter(supportFragmentManager, binding.mainMenuLayout.tbPhase.tabCount, work)
                binding.mainMenuLayout.vpPhases.adapter = mDynamicFragmentAdapter
                binding.mainMenuLayout.vpPhases.currentItem = 0


            }
        } else {
            ToastyWidget.getInstance()
                .displayError(this@HomeActivity, "Workflow not loaded", Toast.LENGTH_SHORT)
        }


    }


/*Return from Select Workflow Activity Logic*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode === 0) {
            if (resultCode === Activity.RESULT_OK) {
                val result = data?.getStringExtra("result")
                GlobalPreferences.getinstance(this)
                    .addOrUpdatePreference(GlobalPreferences.KEY.WORKFLOW, result)
                if (result != null) {
                    val selectedWorkFlow = GlobalPreferences.getinstance(this)
                        .findPrferenceValue(GlobalPreferences.KEY.WORKFLOWUUID, "")
                    Global.WORKFLOWUUID = selectedWorkFlow
                    getWorkFlowsAndBindAlongWithPhases(selectedWorkFlow)
                }
                Toast.makeText(this, "Workflow Changed to: " + result, Toast.LENGTH_SHORT).show()
            }
            if (resultCode === Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode === 112) {
            fillPatientInfoBar()
        } else if (requestCode === 113) {
            if (resultCode === Activity.RESULT_OK) {
                val result = data?.getBooleanExtra("result", false)
                if (result!!) {
                    makeFolder()
                    importDB()
                }

            }
        } else if (requestCode === 114) {
            val result = data?.getBooleanExtra("result", false)
            if (result!!) {
                makeFolder()
                exportDB()
            }
        }
    }


    private fun logoutDialog() {
        val dialog = AlertDialog.Builder(this).setMessage("Are you sure to logout?")
            .setTitle("Are you sure?").setNegativeButton("No", null)
            .setPositiveButton("Yes") { dialog, which ->

                SessionManager(applicationContext).logoutUser()
                GlobalPreferences.getinstance(this)
                    .addOrUpdatePreference(GlobalPreferences.KEY.WORKFLOW, null)
                Global.patientData = null
                finish()

            }
        dialog.show()
    }


    override fun onResume() {

        super.onResume()


        var shouldLogout = checkInActivity()

        if (!shouldLogout) {
            updateActivityTime()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        loadFormDataInEncounterTypes()
        fillPatientInfoBar()
        // requestPermissions()
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
        networkProgressDialog.show("Syncing Data. Please Wait...")
        sessionManager.dataDownloadedCompletely(false)
        metaDataHelper.getAllMetaData(object : RESTCallback {
            override fun <T> onSuccess(o: T) {
                val isSuccess = o as Boolean
                if (isSuccess) {

                    mDynamicFragmentAdapter.notifyDataSetChanged()
                    ToastyWidget.getInstance()
                        .displaySuccess(this@HomeActivity, getString(R.string.sync_success), Toast.LENGTH_LONG)
                    networkProgressDialog.dismiss()
                    sessionManager.dataDownloadedCompletely(true)
                    finish();
                    startActivity(getIntent())

                }

            }

            override fun onFailure(t: Throwable) {

                sessionManager.dataDownloadedCompletely(false)
                ToastyWidget.getInstance()
                    .displayError(this@HomeActivity, getString(R.string.sync_error), Toast.LENGTH_LONG)
                networkProgressDialog.dismiss()
            }
        })


    }

    fun initSystemSettings() {
        val identifierFormat = DataAccess.getInstance()
            .fetchSystemSettingsByUUID(this, "9b68a10b-3ede-43f6-b019-d0823e28ebd1")  //UUID for hydra.IdentifierFormat
        val dateFormat = DataAccess.getInstance()
            .fetchSystemSettingsByUUID(this, "6a78a10b-3eae-43f6-b019-d0823e28ebd1")  //UUID for hydra.dateFormat
        val countryName = DataAccess.getInstance()
            .fetchSystemSettingsByUUID(this, "3h98a10f-3edz-43f6-b020-d0823e28ebd1")  //UUID for hydra.contry


        if (!sessionManager.isCompleteDataDownloaded() || null == identifierFormat || identifierFormat.value.equals(null)) {
            ToastyWidget.getInstance()
                .displayError(this, getString(R.string.need_to_sync), Toast.LENGTH_LONG)
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            Global.identifierFormat = identifierFormat.value
            Global.setDateFormat(dateFormat.value)
            Global.currentCountry = countryName.value
        }
    }


    @Throws(JSONException::class) private fun startForm(patientData: PatientData, bundle: Bundle) {
        var bundle: Bundle? = bundle
        if (bundle == null) bundle = Bundle()
        bundle.putSerializable(ParamNames.DATA, patientData)
        val intent = Intent(this, Form::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }


    fun checkSessionTimeOut() {

        val storedTime = GlobalPreferences.getinstance(this)
            .findPrferenceValue(GlobalPreferences.KEY.ACTIVE_TIME, null)

        if (null != storedTime) {
            val cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"))
            val currentLocalTime = cal.time
            val date: DateFormat = SimpleDateFormat("HH:mm")

            date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"))

            val localTime = date.format(currentLocalTime)
        }
        //     val difference =

//            if (difference > 8) {
//                ToastyWidget.getInstance().displayError(this, "Session Timeout", Toast.LENGTH_LONG)
//                startActivity(Intent(this, LoginActivity::class.java))
//            }
//        } else {
//            ToastyWidget.getInstance().displayError(this, "Session Timeout", Toast.LENGTH_LONG)
//            startActivity(Intent(this, LoginActivity::class.java))
//        }
    }


    private fun updateActivityTime() {
        var currentTime = Date().time

        GlobalPreferences.getinstance(this)
            .addOrUpdatePreference(GlobalPreferences.KEY.ACTIVE_TIME, currentTime)
    }


    fun checkInActivity(): Boolean {

        var currentTime = Date().time
        var toReturn = false

        val storedTime = GlobalPreferences.getinstance(this)
            .findPrferenceValue(GlobalPreferences.KEY.ACTIVE_TIME, currentTime)

        if (currentTime - storedTime > Global.INACTIVITY_LIMIT_MILISECOND) {
            toReturn = true
        }
        return toReturn

    }

    fun setLanguage() {
        var language = GlobalPreferences.getinstance(application)
            .findPrferenceValue(GlobalPreferences.KEY.APP_LANGUAGE, "en")
        Global.APP_LANGUAGE = language
    }

    private fun openSettingDialog() {

        val fragmentManager = supportFragmentManager.beginTransaction()
        val settingFragment = DisplaySettingsDialogFragment.newInstance()
        settingFragment.show(fragmentManager, "settingDialog");
    }

    // slide the view from below itself to the current position
    fun slideUp(view: View) {
        view.visibility = View.VISIBLE
        val animate = TranslateAnimation(0f,  // fromXDelta
            0f,  // toXDelta
            0f,  // fromYDelta
            view.height.toFloat()) // toYDelta
        animate.setDuration(500)
        animate.setFillAfter(true)
        view.startAnimation(animate)
    }

    // slide the view from its current position to below itself
    fun slideDown(view: View) {
        val animate = TranslateAnimation(0f,  // fromXDelta
            0f,  // toXDelta
            view.height.toFloat(),  // fromYDelta
            0f) // toYDelta
        animate.setDuration(500)
        animate.setFillAfter(true)
        view.startAnimation(animate)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(0) fun requestPermissions() {
        var perms = arrayOf(Manifest.permission.MODIFY_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (EasyPermissions.hasPermissions(this, *perms)) {

        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.needs_access_phne_state_and_storage), 0, *perms);
        }
    }
}