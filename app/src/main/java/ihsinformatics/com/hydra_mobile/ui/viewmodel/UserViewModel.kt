package ihsinformatics.com.hydra_mobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import ihsinformatics.com.hydra_mobile.data.local.dao.UserDao
import ihsinformatics.com.hydra_mobile.data.local.entities.User
import ihsinformatics.com.hydra_mobile.data.remote.service.UserApiService
import ihsinformatics.com.hydra_mobile.data.repository.UserRepository
import javax.inject.Inject

class UserViewModel() : ViewModel() {

    //Todo:
    //    private var repository: UserRepository
    //
    //    @Inject
    //    constructor(userDao: UserDao, userApiService: UserApiService) : this() {
    //        repository = UserRepository(application)
    //    }
    //
    //    fun userAuth(username: String, password: String) {
    //        repository.userAuthentication(username, password)
    //    }


}