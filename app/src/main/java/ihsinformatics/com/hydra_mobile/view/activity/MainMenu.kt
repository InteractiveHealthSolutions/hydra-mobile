package ihsinformatics.com.hydra_mobile.view.activity

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.core.FormMenu
import ihsinformatics.com.hydra_mobile.utils.AppUtility
import ihsinformatics.com.hydra_mobile.utils.GridSpacingItemDecoration
import ihsinformatics.com.hydra_mobile.utils.ParamNames
import ihsinformatics.com.hydra_mobile.view.widgets.controls.adapters.MenuAdapter
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.startActivity
import java.util.ArrayList

class MainMenu : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var toolbar: Toolbar
    private var formList: MutableList<FormMenu> = ArrayList<FormMenu>() as MutableList<FormMenu>
    private lateinit var adapter: MenuAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        initNavigationDrawer()
        initRecyclerView()
        prepareForms()

    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    private fun initRecyclerView() {
        var recyclerView = main_screen_recycler_view as RecyclerView
        adapter = MenuAdapter(this, formList)
        val mLayoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, AppUtility.dpToPx(getResources(), 10), true))
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
    }

    private fun initNavigationDrawer() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        var drawerArrow = DrawerArrowDrawable(this)
        drawerArrow.color = ContextCompat.getColor(this@MainMenu, R.color.colorWhite)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        toggle.isDrawerIndicatorEnabled = true
        toggle.drawerArrowDrawable = drawerArrow!!
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
    }


    private fun prepareForms() {
        val thumbnail = intArrayOf(
            R.drawable.patient,
            R.drawable.form2,
            R.drawable.info2,
            R.drawable.screening2,
            R.drawable.form3,
            R.drawable.surgeon,
            R.drawable.info,
            R.drawable.scoring,
            R.drawable.form,
            R.drawable.form,
            R.drawable.form,
            R.drawable.picture
        )
        // formList = mutableListOf<FormMenu>()
        formList.clear()
        formList.add(FormMenu(ParamNames.ENCOUNTER_TYPE_PATIENT_CREATION, thumbnail[0]))
        formList.add(FormMenu(ParamNames.ENCOUNTER_TYPE_PRE_OP_DEMOGRAPHICS, thumbnail[1]))
        formList.add(FormMenu(ParamNames.ENCOUNTER_TYPE_PATIENT_CREATION, thumbnail[2]))
        adapter.notifyDataSetChanged()

/*    val selectedProgram =
        GlobalPreferences.getinstance(this).findPrferenceValue(GlobalPreferences.KEY.PROGRAM, null)
    if (selectedProgram == null) {
        startActivityForResult(Intent(this, SelectProgram::class.java), 0)
        return
    }
    formList!!.clear()
    if (selectedProgram == "SSI") {
        formList!!.add(FormMenu(ParamNames.ENCOUNTER_TYPE_PATIENT_CREATION, thumbnail[0]))
        formList!!.add(FormMenu(ParamNames.ENCOUNTER_TYPE_PRE_OP_DEMOGRAPHICS, thumbnail[1]))
        formList!!.add(FormMenu(ParamNames.ENCOUNTER_TYPE_POST_OP_DEMOGRAPHICS, thumbnail[2]))
        formList!!.add(FormMenu(ParamNames.ENCOUNTER_TYPE_SCREENING_CALL_IN, thumbnail[3]))
        formList!!.add(FormMenu(ParamNames.ENCOUNTER_TYPE_POST_OP_FOLLOW_UP, thumbnail[4]))
        formList!!.add(FormMenu(ParamNames.ENCOUNTER_TYPE_SURGICAL_SITE_EVALUATION, thumbnail[5]))
        formList!!.add(FormMenu(ParamNames.ENCOUNTER_TYPE_SSI_DETECTION, thumbnail[6]))
    } else if (selectedProgram == "Pehla Qadam") {
        formList!!.add(FormMenu(ParamNames.ENCOUNTER_TYPE_PATIENT_CREATION, thumbnail[0]))
        formList!!.add(FormMenu(ParamNames.ENCOUNTER_TYPE_DEMOGRAPHIC_INFORMATION, thumbnail[6]))
        formList!!.add(FormMenu(ParamNames.ENCOUNTER_TYPE_PIRANI_SCORING, thumbnail[7]))
    } else if (selectedProgram == "Safe Circumcision") {
        formList!!.add(FormMenu(ParamNames.ENCOUNTER_TYPE_PATIENT_CREATION, thumbnail[0]))

    }

    //  formList.add(new FormMenu(ParamNames.PATIENT_IMAGES, thumbnail[11]));
    formList.add(FormMenu(ParamNames.ENCOUNTER_TYPE_PRE_CIRCUMCISION, thumbnail[8]));
    formList.add(FormMenu(ParamNames.ENCOUNTER_TYPE_AFTER_CIRCUMCISION, thumbnail[9]));
    formList.add(FormMenu(ParamNames.ENCOUNTER_TYPE_AFTER_CIRCUMCISION2, thumbnail[10]));*//*
        adapter.notifyDataSetChanged()*/
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_notifications -> {
                startActivity(Intent(this, NotificationActivity::class.java))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_tools -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
