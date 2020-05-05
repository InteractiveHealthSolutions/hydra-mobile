package ihsinformatics.com.hydra_mobile.data.repository

import android.app.Application
import android.content.Intent
import android.widget.Toast
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.local.dao.UserDao
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting
import ihsinformatics.com.hydra_mobile.data.local.entities.User
import ihsinformatics.com.hydra_mobile.data.remote.model.user.ProviderApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.user.UserResponse
import ihsinformatics.com.hydra_mobile.data.remote.service.ProviderApiService
import ihsinformatics.com.hydra_mobile.ui.activity.HomeActivity
import ihsinformatics.com.hydra_mobile.utils.GlobalPreferences
import ihsinformatics.com.hydra_mobile.utils.SessionManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(application: Application) {

    private var application: Application
    private var userDao: UserDao

    //TODO :
//        constructor(application: Application,userDao: UserDao,userApiService: UserApiService):this(application){
//                this.userDao =userDao
//        }

    init {
        val database: AppDatabase = AppDatabase.getInstance(application.applicationContext)!!
        this.application = application
        userDao = database.getUserDao()
    }

    fun userAuthentication(username: String, password: String, restCallback: RESTCallback) {
        RequestManager(application, username, password).authenticateUser(username, Constant.REPRESENTATION, object : RESTCallback {
            override fun onFailure(t: Throwable) {
                restCallback.onFailure(t)
            }

            override fun <T> onSuccess(o: T) {

                var userResponse = o as UserResponse

                val providerFetcher = RequestManager(application, username, password).retrofit.create(ProviderApiService::class.java)
                var delimiter = "."
                var userSplit = username.split(delimiter)
                providerFetcher.getProviderData(userSplit[0].toString()).enqueue(object : Callback<ProviderApiResponse> {
                    override fun onFailure(
                        call: Call<ProviderApiResponse>, t: Throwable
                                          ) {
                        ToastyWidget.getInstance().displayError(application, application.getString(R.string.internet_issue), Toast.LENGTH_LONG)
                        restCallback.onFailure(t)
                    }

                    override fun onResponse(
                        call: Call<ProviderApiResponse>, response: Response<ProviderApiResponse>
                                           ) {
                        if (response.isSuccessful && null!=response.body() && response.body()!!.providerResult.size>0) {


                            GlobalPreferences.getinstance(application).addOrUpdatePreference(GlobalPreferences.KEY.PROVIDER, response.body()!!.providerResult[0].uuid)


                            if (userResponse != null) {
                                for (item in userResponse.userList) {
                                    //TODO before insertion of user fetch provider uuid
                                    insertUser(User(item.uuid,item.username, password, response.body()!!.providerResult[0].uuid))
                                    GlobalPreferences.getinstance(application).addOrUpdatePreference(GlobalPreferences.KEY.USERUUID, item.uuid)   // setting uuid only for last user. Technically only one user should come up on response. Therefore, technically this loop must run only once
                                }
                            }
                            restCallback.onSuccess(o)
                        }
                        else
                        {
                            ToastyWidget.getInstance().displayError(application,application.getString(R.string.provider_rights),Toast.LENGTH_SHORT)
                            restCallback.onFailure(Throwable("Cannot find provider"))
                        }
                    }


                })

            }
        })
    }


    suspend fun getUserList(): List<User> {
        var appSettingList = GlobalScope.async {
            userDao.getAllUser()
        }
        return appSettingList.await()
    }

    fun getUserByUsernameAndPass(username:String ,password:String):List<User>
    {
        return userDao.getUserByUsernameAndPass(username,password)
    }

    fun insertUser(user: User) {
        doAsync {
            userDao.insertUser(user)
        }
    }

    fun updateUser(appSetting: AppSetting) {
        doAsync {}
    }

    fun deleteAllUsers()
    {
        userDao.deleteAllUsers()

    }

}