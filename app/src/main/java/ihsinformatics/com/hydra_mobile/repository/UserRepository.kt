package ihsinformatics.com.hydra_mobile.repository

import android.app.Application
import com.google.android.gms.tasks.Tasks.await
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.local.dao.AppSettingDao
import ihsinformatics.com.hydra_mobile.data.local.dao.UserDao
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import ihsinformatics.com.hydra_mobile.data.local.entities.User
import ihsinformatics.com.hydra_mobile.data.remote.model.user.Role
import ihsinformatics.com.hydra_mobile.data.remote.model.user.UserResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.jetbrains.anko.doAsync

class UserRepository(application: Application) {

    private var application: Application
    private var userDao: UserDao

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

                    var userResponse = o as UserResponse
                    for (item in userResponse.results) {
                        var role: Role? = null
                        if (item.roles.isNotEmpty()) {
                            role = item.roles[0]
                        }
                        insertUser(
                            User(
                                item.display,
                                role,
                                item.uuid,
                                item.systemId
                            )
                        )
                    }
                    restCallback.onSuccess(o)
                }
            })
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