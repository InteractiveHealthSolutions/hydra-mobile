package ihsinformatics.com.hydra_mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ihsinformatics.com.hydra_mobile.data.repository.PatientRepository
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.Patient

class PatientViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: PatientRepository = PatientRepository(application)


    fun  search(query: String){
        repository.searchPatientByQuery(query)
    }

    fun insert(patient: Patient) {
        repository.insertPatient(patient)
    }

    fun update(patient: Patient) {
        repository.updatePatient(patient)
    }

    fun getAllPatient(): List<Patient> {
        return repository.getAllPatient()
    }

    fun getPatientByIdentifier(id:String): List<Patient> {
        return repository.getPatientByIdentifier(id)
    }
}