package ihsinformatics.com.hydra_mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ihsinformatics.com.hydra_mobile.data.local.entities.User
import ihsinformatics.com.hydra_mobile.data.repository.PatientRepository
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.Patient
import ihsinformatics.com.hydra_mobile.data.repository.UserRepository

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: UserRepository = UserRepository(application)


    fun getUserByUsernameAndPass(name:String,pass:String): List<User> {
        return repository.getUserByUsernameAndPass(name,pass)
    }
}