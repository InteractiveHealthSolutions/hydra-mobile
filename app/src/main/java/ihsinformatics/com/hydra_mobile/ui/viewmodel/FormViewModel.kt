package ihsinformatics.com.hydra_mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Forms
import ihsinformatics.com.hydra_mobile.data.repository.FormRepository


class FormViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: FormRepository = FormRepository(application)


    fun getAllForms(): List<Forms> {
        return repository.getAllForms()
    }


}