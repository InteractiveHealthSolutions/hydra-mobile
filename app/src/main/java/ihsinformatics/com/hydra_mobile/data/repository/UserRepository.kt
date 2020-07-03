package ihsinformatics.com.hydra_mobile.data.repository

import android.app.Application
import android.widget.Toast
import com.ihsinformatics.dynamicformsgenerator.data.pojos.DataAccess
import com.ihsinformatics.dynamicformsgenerator.data.pojos.User
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.R
import ihsinformatics.com.hydra_mobile.data.remote.manager.RequestManager
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.local.AppDatabase
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.remote.model.user.ProviderApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.user.UserResponse
import ihsinformatics.com.hydra_mobile.data.remote.service.ProviderApiService
import ihsinformatics.com.hydra_mobile.utils.GlobalPreferences
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(application: Application) {

    private var application: Application

    init {
        val database: AppDatabase = AppDatabase.getInstance(application.applicationContext)!!
        this.application = application
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
                                    insertUser(User(item.uuid,item.username, password, response.body()!!.providerResult[0].uuid,null))
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
            DataAccess.getInstance().getAllUser(application)
        }
        return appSettingList.await()
    }

    fun getUserByUsernameAndPass(username:String ,password:String):List<User>
    {
        return DataAccess.getInstance().getUserByUsernameAndPass(application,username,password)
    }

    fun insertUser(user: User) {
        doAsync {
            DataAccess.getInstance().insertUser(application,user)
        }
    }

    fun deleteAllUsers()
    {
        DataAccess.getInstance().deleteAllUsers(application)

    }

}