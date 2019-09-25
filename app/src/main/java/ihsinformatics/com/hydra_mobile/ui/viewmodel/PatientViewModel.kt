package ihsinformatics.com.hydra_mobile.ui.viewmodel

import android.app.Application
import android.app.DownloadManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ihsinformatics.com.hydra_mobile.data.local.entities.Patient
import ihsinformatics.com.hydra_mobile.data.repository.PatientRepository

class PatientViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: PatientRepository = PatientRepository(application)


    private var patientList: LiveData<List<Patient>> = repository.getAllPatient()

    fun  search(query: String){
        repository.searchPatientByIdentifier(query)
    }

    fun insert(patient: Patient) {
        repository.insertPatient(patient)
    }

    fun update(patient: Patient) {
        repository.updatePatient(patient)
    }

    fun getAllPatient(): LiveData<List<Patient>> {
        return patientList
    }


}