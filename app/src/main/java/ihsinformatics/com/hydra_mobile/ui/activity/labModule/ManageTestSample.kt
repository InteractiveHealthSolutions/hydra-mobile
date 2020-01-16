package ihsinformatics.com.hydra_mobile.ui.activity.labModule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.ui.adapter.CommonLabAdapter
import ihsinformatics.com.hydra_mobile.ui.adapter.ManageTestSampleAdapter

class ManageTestSample : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_test_sample)

        val rv = findViewById<RecyclerView>(R.id.manageTestSample)

        val testTypeList = ArrayList<String>()

        testTypeList.add("Fake Text 1")
        testTypeList.add("Fake Text 2")
        testTypeList.add("Fake Text 3")
        testTypeList.add("Fake Text 4")
        testTypeList.add("Fake Text 5")
        testTypeList.add("Fake Text 6")
        testTypeList.add("Fake Text 7")


        var adapter =
            ManageTestSampleAdapter(
                testTypeList, this
            )
        rv.adapter = adapter

        adapter.updateTestOrderList(testTypeList)

    }
}
