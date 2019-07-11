package ihsinformatics.com.hydra_mobile.ui.activity

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.core.FormMenu
import ihsinformatics.com.hydra_mobile.databinding.ActivityMainMenuBinding
import ihsinformatics.com.hydra_mobile.utils.AppUtility
import ihsinformatics.com.hydra_mobile.utils.GridSpacingItemDecoration
import ihsinformatics.com.hydra_mobile.utils.ParamNames
import ihsinformatics.com.hydra_mobile.ui.widgets.controls.adapters.MenuAdapter
import kotlinx.android.synthetic.main.app_bar_main_menu.view.*
import java.util.ArrayList


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var toolbar: Toolbar
    private var formList: MutableList<FormMenu> = ArrayList<FormMenu>() as MutableList<FormMenu>
    private lateinit var adapter: MenuAdapter
    lateinit var binding: ActivityMainMenuBinding
    lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_menu)
        title = ""
        toolbar = binding.mainMenuLayout.toolbar
        setSupportActionBar(toolbar)
        initNavigationDrawer()
        initRecyclerView()
        initToolbar()
        prepareForms()
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
        var recyclerView = binding.mainMenuLayout.contentMenu.mainScreenRecyclerView
        adapter = MenuAdapter(this, formList, MenuAdapter.OnItemClickListener {
            menuListener(it)
        })
        val mLayoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, AppUtility.dpToPx(getResources(), 10), true))
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
    }

    private fun menuListener(item: FormMenu) {
        var encounterName: String? = null
        if (item.name == ParamNames.ENCOUNTER_TYPE_PATIENT_CREATION) run {
            encounterName = ParamNames.ENCOUNTER_TYPE_PATIENT_CREATION

            Toast.makeText(this, "Encounter" + encounterName, Toast.LENGTH_SHORT).show()
               // startActivity(Intent(this, Form::class.java))
              //  Form.setENCOUNTER_NAME(encounterName)
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
    }

    //Todo: this will change ...
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
    }

    override fun onBackPressed() {
        drawerLayout = binding.drawerLayout
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    /*    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /*   return when (item.itemId) {
               R.id.action_notifications -> {
                   startActivity(Intent(this, NotificationActivity::class.java))
                   return true
               }
               else -> super.onOptionsItemSelected(item)
           }*/
        return super.onOptionsItemSelected(item)
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


    private fun animateToggleButton(start: Float, end: Float) {
        val anim = ValueAnimator.ofFloat(start, end)
        anim.addUpdateListener { valueAnimator ->
            toggle?.onDrawerSlide(
                binding.drawerLayout,
                valueAnimator.animatedValue as Float
            )
        }
        anim.interpolator = DecelerateInterpolator() as TimeInterpolator?
        anim.duration = 250
        anim.start()
    }

}
