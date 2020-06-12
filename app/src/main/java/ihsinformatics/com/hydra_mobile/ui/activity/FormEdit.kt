package ihsinformatics.com.hydra_mobile.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess
import com.ihsinformatics.dynamicformsgenerator.data.database.SaveableForm
import com.ihsinformatics.dynamicformsgenerator.utils.Global
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.ui.adapter.EditFormsAdapter

class FormEdit : AppCompatActivity() {


    private lateinit var offlineFormsRecyclerView: RecyclerView
    private lateinit var editFormsAdapter: EditFormsAdapter

    private var saveableOfflineForms = ArrayList<SaveableForm>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_edit)

        offlineFormsRecyclerView = findViewById<RecyclerView>(R.id. offline_forms)
        offlineFormsRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

    }


    fun setFormsList() {

        saveableOfflineForms= DataAccess.getInstance().getAllFormsByHydraUrl(this, Global.HYRDA_CURRENT_URL) as ArrayList<SaveableForm>
        editFormsAdapter = EditFormsAdapter(saveableOfflineForms,this)
        offlineFormsRecyclerView.adapter = editFormsAdapter

    }

    override fun onResume() {
        super.onResume()
        setFormsList()
    }
}
