package ihsinformatics.com.hydra_mobile.data.repository

import android.app.Application
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.local.dao.UserDao
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import ihsinformatics.com.hydra_mobile.data.local.entities.User
import ihsinformatics.com.hydra_mobile.data.remote.service.UserApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.jetbrains.anko.doAsync

class UserRepository(application: Application) {

    private var application: Application
    private var userDao: UserDao

    //TODO :
//        constructor(application: Application,userDao: UserDao,userApiService: UserApiService):this(application){
//                this.userDao =userDao
//        }

    init {
        val database: AppDatabase = AppDatabase.getInstance(
            application.applicationContext
        )!!
        this.application = application
        userDao = database.getUserDao()
    }

    fun userAuthentication(username: String, password: String, restCallback: RESTCallback) {
        RequestManager(application, username, password).authenticateUser(
            username,
            Constant.REPRESENTATION,
            object : RESTCallback {
                override fun onFailure(t: Throwable) {
                    restCallback.onFailure(t)
                }

                override fun onSuccess(o: Any) {
                    //
                    //                    var userResponse = o as UserResponse
                    //                    for (item in userResponse.results) {
                    //                        var role: Role? = null
                    //                        if (item.roles.isNotEmpty()) {
                    //                            role = Role(item.roles[0].display, item.roles[0].description)
                    //                        }
                    //                        insertUser(
                    //                            User(
                    //                                item.display,
                    //                                role,
                    //                                item.uuid,
                    //                                item.systemId
                    //                            )
                    //                        )
                    //                    }
                    restCallback.onSuccess(o)
                }
            })
    }

    fun loadUser(username: String,password: String){


    }


    suspend fun getUserList(): List<User> {
        var appSettingList = GlobalScope.async {
            userDao.getAllUser()
        }
        return appSettingList.await()
    }

    fun insertUser(user: User) {
        doAsync {
            userDao.insertUser(user)
        }
    }

    fun updateUser(appSetting: AppSetting) {
        doAsync {
        }
    }

}