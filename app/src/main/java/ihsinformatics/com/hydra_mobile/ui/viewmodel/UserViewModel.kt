package ihsinformatics.com.hydra_mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ihsinformatics.dynamicformsgenerator.data.pojos.User
import ihsinformatics.com.hydra_mobile.data.repository.UserRepository

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: UserRepository = UserRepository(application)


    fun getUserByUsernameAndPass(name:String,pass:String): List<User> {
        return repository.getUserByUsernameAndPass(name,pass)
    }

    fun deleteAllUsers()
    {
        repository.deleteAllUsers()
    }

}