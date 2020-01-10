package ihsinformatics.com.hydra_mobile.ui.activity


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.ihsinformatics.dynamicformsgenerator.PatientInfoFetcher
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import com.luseen.spacenavigation.SpaceItem
import com.luseen.spacenavigation.SpaceNavigationView
import com.luseen.spacenavigation.SpaceOnClickListener
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.databinding.ActivityHomeBinding
import ihsinformatics.com.hydra_mobile.ui.activity.labModule.CommonLabActivity
import ihsinformatics.com.hydra_mobile.ui.adapter.DynamicFragmentAdapter
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import ihsinformatics.com.hydra_mobile.ui.viewmodel.WorkflowPhasesMapViewModel
import ihsinformatics.com.hydra_mobile.utils.GlobalPreferences
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import kotlinx.android.synthetic.main.app_bar_main_menu.view.*
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.PeriodType


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*screen level initialization*/



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

        fillPatientInfoBar()

        /*user level initialization*/

        //session initialization
        if (sessionManager.isFirstTime()) {
            sessionManager.checkFirstTimeInstall(false)
        }


        //previous workflow check (if set then continue and fetch phases for that workflow)
        var selectedWorkFlow = GlobalPreferences.getinstance(this)
            .findPrferenceValue(GlobalPreferences.KEY.WORKFLOWUUID, null)
        selectedWorkFlow="Taha"
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
        spaceNavigationView.addSpaceItem(
            SpaceItem(
                resources.getString(R.string.common_lab),
                R.drawable.ic_testtubes
            )
        )
        spaceNavigationView.addSpaceItem(
            SpaceItem(
                resources.getString(R.string.patient_summary),
                R.drawable.ic_form
            )
        )



        spaceNavigationView.setSpaceOnClickListener(object : SpaceOnClickListener {

            override fun onCentreButtonClick() {

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
            resources.getString(R.string.patient_summary) -> {

                if (Global.patientData == null) {
                    ToastyWidget.getInstance()
                        .displayWarning(this@HomeActivity, "Patient Not Loaded", Toast.LENGTH_SHORT)
                } else {
                    startActivity(Intent(applicationContext, ProfileActivity::class.java))
                    finish()
                }
            }

            resources.getString(R.string.common_lab) -> {

                startActivity(Intent(applicationContext, CommonLabActivity::class.java))
                finish()
            }

        }
    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    private fun initToolbar() {
        toolbar.action_notification_.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
            finish()
        })


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
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
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
            R.id.nav_setting -> {

            }
            R.id.nav_language -> {

            }
            R.id.nav_forms -> {

            }
            R.id.nav_faq -> {
                startActivity(Intent(this, HelpActivity::class.java))
                finish()
            }
            R.id.nav_events -> {
                startActivity(Intent(this, EventsActivity::class.java))
                finish()
            }
            R.id.nav_reports -> {
                startActivity(Intent(this, ReportActivity::class.java))
                finish()
            }
            R.id.nav_search -> {
                PatientInfoFetcher.init(
                    Constant.formName,
                    PatientInfoFetcher.REQUEST_TYPE.FETCH_INFO
                )
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

    private fun initPhase() {
        binding.mainMenuLayout.vpPhases.offscreenPageLimit = 2
        binding.mainMenuLayout.vpPhases.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                binding.mainMenuLayout.tbPhase
            )
        )
        binding.mainMenuLayout.tbPhase.setOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {

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


        val workflowPhaseMapViewModel =
            ViewModelProviders.of(this).get(WorkflowPhasesMapViewModel::class.java)
//        var selectedWorkFlow = GlobalPreferences.getinstance(this)
//            .findPrferenceValue(GlobalPreferences.KEY.WORKFLOWUUID, "")
        if (null != selectedWorkFlow && !selectedWorkFlow.equals("")) {
            val work = workflowPhaseMapViewModel.getPhasesByWorkFlowUUID(selectedWorkFlow)


            //TODO remove repository and try viewModel
            // work = workflowPhasesRepository.getPhasesByWorkFlowName(selectedWorkFlow)
            binding.mainMenuLayout.tbPhase.removeAllTabs()
            binding.mainMenuLayout.vpPhases.removeAllViews()
            if (work.isNotEmpty()) {

                for (element in work) {
                    binding.mainMenuLayout.tbPhase.addTab(
                        binding.mainMenuLayout.tbPhase.newTab().setText(
                            "" + element.phaseName
                        )
                    )
                }


                mDynamicFragmentAdapter =
                    DynamicFragmentAdapter(
                        supportFragmentManager,
                        binding.mainMenuLayout.tbPhase.tabCount,
                        work
                    )
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
        val dialog = AlertDialog.Builder(this)
            .setMessage("Are you sure to logout?")
            .setTitle("Are you sure?")
            .setNegativeButton("No", null)
            .setPositiveButton(
                "Yes"
            ) { dialog, which ->

                SessionManager(applicationContext).logoutUser()
                GlobalPreferences.getinstance(this)
                    .addOrUpdatePreference(GlobalPreferences.KEY.WORKFLOW, null)
                finish()

            }
        dialog.show()
    }

}