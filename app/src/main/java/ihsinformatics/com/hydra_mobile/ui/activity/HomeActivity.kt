package ihsinformatics.com.hydra_mobile.ui.activity


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.widget.Toast
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.android.material.tabs.TabLayout
import com.luseen.spacenavigation.SpaceItem
import com.luseen.spacenavigation.SpaceNavigationView
import com.luseen.spacenavigation.SpaceOnClickListener
import com.roger.catloadinglibrary.CatLoadingView
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.core.FormMenu
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Phases
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesMap
import ihsinformatics.com.hydra_mobile.data.services.manager.SyncData
import ihsinformatics.com.hydra_mobile.data.services.worker.WorkflowWorker
import ihsinformatics.com.hydra_mobile.databinding.ActivityHomeBinding
import ihsinformatics.com.hydra_mobile.ui.adapter.DynamicFragmentAdapter
import ihsinformatics.com.hydra_mobile.ui.base.BaseActivity
import ihsinformatics.com.hydra_mobile.ui.dialogs.LoadingProgressDialog
import ihsinformatics.com.hydra_mobile.ui.viewmodel.PhasesViewModel
import ihsinformatics.com.hydra_mobile.ui.viewmodel.WorkflowPhasesViewModel
import ihsinformatics.com.hydra_mobile.utils.ParamNames
import ihsinformatics.com.hydra_mobile.ui.widgets.controls.adapters.MenuAdapter
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import kotlinx.android.synthetic.main.app_bar_main_menu.view.*
import java.util.ArrayList


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                finish()
            }
            R.id.img_events -> {
                startActivity(Intent(this, EventsActivity::class.java))
                finish()
            }
            R.id.img_report -> {
                startActivity(Intent(this, ReportActivity::class.java))
                finish()
            }
            R.id.fab_profile -> {
                Toast.makeText(this, "not implemented", Toast.LENGTH_SHORT).show()
            }


        }
    }

    lateinit var sessionManager: SessionManager
    lateinit var spaceNavigationView: SpaceNavigationView
    private lateinit var toolbar: Toolbar
    private var formList: MutableList<FormMenu> = ArrayList<FormMenu>() as MutableList<FormMenu>
    private lateinit var adapter: MenuAdapter
    lateinit var binding: ActivityHomeBinding
    lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        title = ""
        toolbar = binding.mainMenuLayout.toolbar
        setSupportActionBar(toolbar)
        initNavigationDrawer()
        initToolbar()
        initListener()
        sessionManager = SessionManager(this)
        if (sessionManager.isFirstTime()) {
            openLoadingScreen()
        } else {
            getWorkflow()
        }

        spaceNavigationView = findViewById<SpaceNavigationView>(R.id.space)
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(SpaceItem("Form", R.drawable.ic_form_filled));
        spaceNavigationView.addSpaceItem(SpaceItem("Search", R.drawable.ic_search));
        spaceNavigationView.addSpaceItem(SpaceItem("Report", R.drawable.ic_report_filled));
        spaceNavigationView.addSpaceItem(SpaceItem("Event", R.drawable.ic_event_filled));

        spaceNavigationView.setSpaceOnClickListener(object : SpaceOnClickListener {

            override fun onCentreButtonClick() {
                Toast.makeText(applicationContext, "user profile", Toast.LENGTH_SHORT).show();
            }

            override fun onItemReselected(itemIndex: Int, itemName: String?) {
                Toast.makeText(applicationContext, "" + itemIndex + " " + itemName, Toast.LENGTH_SHORT).show(); }

            override fun onItemClick(itemIndex: Int, itemName: String?) {
                Toast.makeText(applicationContext, "" + itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }
        })

    }


    private fun initListener() {
        binding.mainMenuLayout.imgSearch.setOnClickListener(this)
        binding.mainMenuLayout.imgEvents.setOnClickListener(this)
        binding.mainMenuLayout.imgReport.setOnClickListener(this)
        binding.mainMenuLayout.fabProfile.setOnClickListener(this)
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

    private fun initRecyclerView() {
        // var recyclerView = binding.mainMenuLayout.contentHost.mainScreenRecyclerView
        adapter = MenuAdapter(this, formList, MenuAdapter.OnItemClickListener {
            menuListener(it)
        })
//                    val mLayoutManager = GridLayoutManager(this, 2)
//                    recyclerView.layoutManager = mLayoutManager
//                    recyclerView.addItemDecoration(GridSpacingItemDecoration(2, AppUtility.dpToPx(getResources(), 10), true))
//                    recyclerView.itemAnimator = DefaultItemAnimator()
//                    recyclerView.adapter = adapter
    }

    private fun menuListener(item: FormMenu) {
        var encounterName: String? = null
        if (item.name == ParamNames.ENCOUNTER_TYPE_PATIENT_CREATION) run {
            encounterName = ParamNames.ENCOUNTER_TYPE_PATIENT_CREATION

            Toast.makeText(this, "Encounter" + encounterName, Toast.LENGTH_SHORT).show()
            // startActivity(Intent(this, ihsinformatics.com.hydra_mobile.data.remote.model.workflow.Form::class.java))
            //  ihsinformatics.com.hydra_mobile.data.remote.model.workflow.Form.setENCOUNTER_NAME(encounterName)
        }
    }

    private fun initNavigationDrawer() {
        val drawerLayout: DrawerLayout = binding.drawerLayout//findViewById(R.id.drawer_layout)
        val navView: NavigationView = binding.navView //findViewById(R.id.nav_view)
        var drawerArrow = DrawerArrowDrawable(this)
        drawerArrow.color = ContextCompat.getColor(this@HomeActivity, R.color.colorWhite)
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
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
            R.id.nav_logout -> {
                SessionManager(applicationContext).logoutUser()
                finish()
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
        //getPhases()

    }

    private fun getPhases() {
        val phasesViewModel = ViewModelProviders.of(this).get(PhasesViewModel::class.java)
        phasesViewModel.getAllPhases().observe(this, Observer<List<Phases>> {
            if (it.isNotEmpty()) {
                for (i in 0 until it.size) {
                    binding.mainMenuLayout.tbPhase.addTab(binding.mainMenuLayout.tbPhase.newTab().setText("" + it[i].name))
//                    binding.mainMenuLayout.tbPhase.getTabAt(i)!!.setIcon(R.drawable.ic_gesture_black_24dp)
                }
                /*  val mDynamicFragmentAdapter =
                      DynamicFragmentAdapter(supportFragmentManager, binding.mainMenuLayout.tbPhase.tabCount, it)
                  binding.mainMenuLayout.vpPhases.adapter = mDynamicFragmentAdapter
                  binding.mainMenuLayout.vpPhases.currentItem = 0*/
            }

        })
    }

    private fun getWorkflow() {
        val workflowPhasesViewModel = ViewModelProviders.of(this).get(WorkflowPhasesViewModel::class.java)
        workflowPhasesViewModel.getAllPWorkflowPhases()
            .observe(this, Observer<List<WorkflowPhasesMap>> { workflowList ->
                if (workflowList.isNotEmpty()) {
                    for (element in workflowList) {
                        binding.mainMenuLayout.tbPhase.addTab(binding.mainMenuLayout.tbPhase.newTab().setText("" + element.phaseName))
                    }

                    val mDynamicFragmentAdapter =
                        DynamicFragmentAdapter(
                            supportFragmentManager,
                            binding.mainMenuLayout.tbPhase.tabCount,
                            workflowList
                        )
                    binding.mainMenuLayout.vpPhases.adapter = mDynamicFragmentAdapter
                    binding.mainMenuLayout.vpPhases.currentItem = 0
                }
            })
    }

    private fun openDialog() {
        val dialog = AlertDialog.Builder(this)
            .setMessage(getString(R.string.are_you_sure_exit_application))
            .setTitle(getString(R.string.are_you_sure))
            .setNegativeButton(getString(R.string.no), null)
            .setPositiveButton(
                getString(R.string.yes)
            ) { _, _ ->
                //SessionManager(applicationContext).logoutUser()
                finishAffinity()
                //
                //        dialog.show()System.exit(0)
            }
        dialog.show()
    }

    private fun openLoadingScreen() {

        val fragmentManager = supportFragmentManager.beginTransaction()
        val loadingScreening = LoadingProgressDialog.newInstance()
        loadingScreening.show(fragmentManager, "loading screening")
        SyncData.instance(this).initWorkManage()

        Handler().postDelayed(
            Runnable {
                kotlin.run {
                    sessionManager.checkFirstTimeInstall(false)
                    loadingScreening.dismiss()
                    getWorkflow()
                }
            }, 5000
        )
    }

}

