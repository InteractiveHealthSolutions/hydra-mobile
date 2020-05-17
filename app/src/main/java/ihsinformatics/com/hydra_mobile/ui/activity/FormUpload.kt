package ihsinformatics.com.hydra_mobile.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ihsinformatics.dynamicformsgenerator.data.database.DataAccess
import com.ihsinformatics.dynamicformsgenerator.data.database.SaveableForm
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.ui.adapter.EditFormsAdapter
import ihsinformatics.com.hydra_mobile.ui.adapter.UploadFormsAdapter

class FormUpload : AppCompatActivity() {

    private lateinit var offlineFormsRecyclerView: RecyclerView
    private lateinit var uploadFormsAdapter: UploadFormsAdapter

    private var saveableOfflineForms = ArrayList<SaveableForm>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_upload)

        offlineFormsRecyclerView = findViewById<RecyclerView>(R.id. offline_forms)
        offlineFormsRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

    }


    fun setFormsList() {

        saveableOfflineForms= DataAccess.getInstance().getAllForms(this) as ArrayList<SaveableForm>
        uploadFormsAdapter = UploadFormsAdapter(saveableOfflineForms,this)
        offlineFormsRecyclerView.adapter = uploadFormsAdapter

    }

    override fun onResume() {
        super.onResume()
        setFormsList()
    }
}