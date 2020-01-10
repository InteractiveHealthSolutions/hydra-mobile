package ihsinformatics.com.hydra_mobile.ui.activity.labModule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.ui.adapter.CustomExpandableTestAdderAdapter
import kotlinx.android.synthetic.main.activity_profile.*

class TestAdder : AppCompatActivity() {

    internal var expandableListView: ExpandableListView? = null
    internal var adapter: ExpandableListAdapter? = null
    internal var titleList: List<String>? = null

    val data: LinkedHashMap<String, List<String>>
        get() {

            val listData = LinkedHashMap<String, List<String>>()

            val cricket = ArrayList<String>();
            cricket.add("India");
            cricket.add("Pakistan");
            cricket.add("Australia");
            cricket.add("England");
            cricket.add("South Africa");

            val football = ArrayList<String>();
            football.add("Brazil");
            football.add("Spain");
            football.add("Germany");
            football.add("Netherlands");
            football.add("Italy");

            val basketball = ArrayList<String>();
            basketball.add("United States");
            basketball.add("Spain");
            basketball.add("Argentina");
            basketball.add("France");
            basketball.add("Russia");

            listData.put("CRICKET TEAMS", cricket);
            listData.put("FOOTBALL TEAMS", football);
            listData.put("BASKETBALL TEAMS", basketball);
            return listData;
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_adder)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        expandableListView = findViewById(R.id.expandableListView)

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val width = metrics.widthPixels

        expandableListView!!.setIndicatorBounds(width - GetPixelFromDips(50f), width - GetPixelFromDips(10f));




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

    }

    fun GetPixelFromDips(pixels: Float): Int {
        // Get the screen's density scale
        val scale = resources.displayMetrics.density
        // Convert the dps to pixels, based on density scale
        return (pixels * scale + 0.5f).toInt()
    }
}
