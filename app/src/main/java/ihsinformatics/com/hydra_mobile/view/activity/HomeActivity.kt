package ihsinformatics.com.hydra_mobile.view.activity

import android.app.AlertDialog
import android.content.*
import android.graphics.Rect
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.core.Form
import ihsinformatics.com.hydra_mobile.data.core.FormMenu
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.PatientData
import ihsinformatics.com.hydra_mobile.utils.AppUtility
import ihsinformatics.com.hydra_mobile.utils.GlobalPreferences
import ihsinformatics.com.hydra_mobile.utils.ParamNames
import ihsinformatics.com.hydra_mobile.view.provider.DataProvider
import ihsinformatics.com.hydra_mobile.view.widgets.controls.adapters.MenuAdapter
import kotlinx.android.synthetic.main.action_notification.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.find
import java.util.ArrayList


class HomeActivity : AppCompatActivity() {

    private lateinit var tvNotification: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var formList: MutableList<FormMenu>
    private lateinit var adapter: MenuAdapter
    private var logout: Boolean = false
    // private Button btnSync;
    private var notificationMenu: PopupMenu? = null
    private var actionNotification: RelativeLayout? = null
    private var notificationItemClickListener: NotificationItemClickListener? = null
    protected var patientData: PatientData? = null
    var isActive = false
    val ACTION_UPDATE_NOTIFICATION = "UPDATE_NOTIFICATION"

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        initCollapsingToolbar()
        recyclerView = main_screen_recycler_view as RecyclerView
        formList = ArrayList<FormMenu>()
        adapter = MenuAdapter(this, formList)
        val mLayoutManager = GridLayoutManager(this, 2)
        recyclerView!!.setLayoutManager(mLayoutManager)
        recyclerView!!.addItemDecoration(GridSpacingItemDecoration(2, AppUtility.dpToPx(getResources(), 10), true))
        recyclerView!!.setItemAnimator(DefaultItemAnimator())
        recyclerView!!.setAdapter(adapter)
        // prepareForms();
        try {
            Glide.with(this).load(R.drawable.app_gradient).into(findViewById(R.id.backdrop) as ImageView)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        notificationItemClickListener = NotificationItemClickListener()
        logout = false
        registerReceiver(broadcastReceiver, IntentFilter(ACTION_UPDATE_NOTIFICATION))
    }


    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private fun initCollapsingToolbar() {
        val collapsingToolbar = collapsing_toolbar as CollapsingToolbarLayout
        collapsingToolbar.setTitle(" ")
        val appBarLayout = appbar as AppBarLayout
        appBarLayout.setExpanded(true)
        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            internal var isShow = false
            internal var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange()
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name))
                    isShow = true
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ")
                    isShow = false
                }
            }
        })
    }

    /**
     * Adding few albums for testing
     */
    private fun prepareForms() {
        /* val thumbnail = intArrayOf(
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
         val selectedProgram =
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
         *//* formList.add(new FormMenu(ParamNames.ENCOUNTER_TYPE_PRE_CIRCUMCISION, thumbnail[8]));
        formList.add(new FormMenu(ParamNames.ENCOUNTER_TYPE_AFTER_CIRCUMCISION, thumbnail[9]));
        formList.add(new FormMenu(ParamNames.ENCOUNTER_TYPE_AFTER_CIRCUMCISION2, thumbnail[10]));*//*
        adapter.notifyDataSetChanged()*/
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    inner class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right =
                    spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        isActive = true
    }

    override fun onStop() {
        super.onStop()
        isActive = false
        //        unregisterReceiver(broadcastReceiver);
    }

    override fun onResume() {
        if (Constant.USERNAME == null || Constant.PASSWORD == null) {
            val username = GlobalPreferences.getinstance(this).findPrferenceValue(GlobalPreferences.KEY.USERNAME, null)
            val password = GlobalPreferences.getinstance(this).findPrferenceValue(GlobalPreferences.KEY.PASSWORD, null)
            if (username != null && password != null) {
                Constant.USERNAME = username
                Constant.PASSWORD = password
            } else {
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                finish()
            }
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        val isFirstRun = GlobalPreferences.getinstance(this).findPrferenceValue(GlobalPreferences.KEY.FIRST_RUN, true)
        if (isFirstRun) {
            GlobalPreferences.getinstance(this).addOrUpdatePreference(GlobalPreferences.KEY.FIRST_RUN, false)
            //Todo :
            // DataAccess.getInstance().inserDefaultData(this)
            //startService(Intent(this@HomeActivity, DataSync::class.java))
        }

        prepareForms()

        val intent = Intent("com.ihsinformatics.DATA_UPLOAD_ATTEMPT")
        sendBroadcast(intent)

        updateUserInfoBar()
        updateNotification()
        super.onResume()
    }

    private fun updateUserInfoBar() {
        val tvUsername = tvUsername as TextView
        val ivGender = findViewById(R.id.ivGender) as ImageView
        tvUsername.setText(Constant.USERNAME)
        //Todo: Data Access layer should change with viewmodel or repository
        /* val user = DataAccess.getInstance().getUserCredentials(Constant.USERNAME)
         if (user.getFullName() != null || user.getGender() != null) {
             tvUsername.setText(user.getFullName())
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                 if (user.getGender().toLowerCase().startsWith("m")) {

                     ivGender.setImageDrawable(getDrawable(R.drawable.male_icon))
                 } else {
                     ivGender.setImageDrawable(getDrawable(R.drawable.female_icon))
                 }
             }
         }*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val item = menu.findItem(R.id.action_notifications);
        MenuItemCompat.setActionView(item, R.layout.action_notification)
        actionNotification = MenuItemCompat.getActionView(item) as RelativeLayout?
        tvNotification = actionNotification!!.tvCount as TextView
        initNotificationsMenu()
        updateNotification()
        return super.onCreateOptionsMenu(menu)
    }

    private fun initNotificationsMenu() {
        notificationMenu = PopupMenu(this, tvNotification)
        // notificationMenu.getMenuInflater().inflate(R.menu.login, notificationMenu.getMenu());
        actionNotification!!.setOnClickListener { notificationMenu!!.show() }
    }

    fun updateNotification() {
        if (tvNotification != null) {
            var sum = 0
            //Todo: Access local database
            /* val dataAccess = DataAccess.getInstance()
             notificationMenu!!.menu.clear()
             val formNames = DataProvider.getInstance(this).getEncounterNames()
             for (i in formNames.indices) {
                 val formName = formNames[i]
                 val count = dataAccess.getAllForms(this, formName).size()
                 val item = notificationMenu!!.menu.add(0, i, i, formName + " ( " + count + " )")
                 item.setOnMenuItemClickListener(notificationItemClickListener)
                 // sum += dataAccess.getAllForms(this, formName).size();
                 sum += count
             }
             if (sum > 0) {
                 tvNotification!!.visibility = View.VISIBLE
                 tvNotification!!.text = String.format("%02d", sum)
             } else {
                 tvNotification!!.visibility = View.GONE
                 tvNotification!!.text = "00"
             }*/
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //Todo :
/*
        val id = item.itemId
        if (id == R.id.action_settings) {
            return true
        } else if (id == R.id.action_logout) {
            logout = true
            GlobalPreferences.getinstance(this).addOrUpdatePreference(GlobalPreferences.KEY.USERNAME, null)
            GlobalPreferences.getinstance(this).addOrUpdatePreference(GlobalPreferences.KEY.PASSWORD, null)
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            finish()
        } else if (id == R.id.action_language) {
            startActivity(Intent(this@HomeActivity, LanguageSelector::class.java))
        } else if (id == R.id.action_notifications) {
            *//*Intent intent = new Intent(MainScreen.this, SavedFormsDisplayActivity.class);
            startActivity(intent);*//*
        } else if (id == R.id.action_location) {
            startActivity(Intent(this@HomeActivity, LocationSelector::class.java))
        } else if (id == R.id.action_sync) {
            startService(Intent(this@HomeActivity, DataSync::class.java))
        }
        return super.onOptionsItemSelected(item)*/
        return super.onOptionsItemSelected(item)
    }

    private inner class NotificationItemClickListener : MenuItem.OnMenuItemClickListener {
        override fun onMenuItemClick(item: MenuItem): Boolean {
            /*Intent intent = new Intent(MainScreen.this, SavedFormsDisplayActivity.class);
            intent.putExtra(ParamNames.ENCOUNER_NAME, item.getOrder());
            startActivity(intent);*/
            return false
        }
    }

    internal var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateNotification()
        }
    }

    override fun onBackPressed() {
        val dialog = AlertDialog.Builder(this)
            .setMessage("Are you sure to exit Application ?")
            .setTitle("Are you sure?")
            .setNegativeButton("No", null)
            .setPositiveButton("Yes") { dialog, which -> this@HomeActivity.finish() }
        dialog.show()
    }
}

