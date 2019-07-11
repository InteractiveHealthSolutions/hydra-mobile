package ihsinformatics.com.hydra_mobile.data.remote.manager

import android.app.Application
import ihsinformatics.com.hydra_mobile.data.remote.model.BasicAuthInterceptor
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.remote.service.UserApiService
import ihsinformatics.com.hydra_mobile.utils.AppConfiguration
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import com.google.gson.Gson
import ihsinformatics.com.hydra_mobile.data.remote.model.user.UserResponse


/**
 * @author  Shujaat ali
 * @Email   shujaat.ali@ihsinformatics.com
 * @version 1.0.0
 * @DateCreated   2019-5-14
 */

class RequestManager {

    var retrofit: Retrofit? = null
    var okHttpClient: OkHttpClient? = null
    lateinit var gson: Gson

    constructor(application: Application, username: String, password: String) {

        initOkHttp(
            username,
            password
        )
        retrofit = Retrofit.Builder()
            .baseUrl("http://test.hydra.com/openmrs/ws/rest/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    private fun initOkHttp(username: String, password: String) {
        val httpClient = OkHttpClient().newBuilder()
        httpClient.addInterceptor(
            BasicAuthInterceptor(
                username,
                password
            )
        )

        okHttpClient = httpClient.build()
    }

    private fun getBaseUrl(application: Application): String {
        return AppConfiguration().getBaseUrl(application)
    }

    fun authenticateUser(username: String, representation: String, restCallback: RESTCallback) {
        val userService = retrofit!!.create(UserApiService::class.java)

        userService.getUser(username, representation).enqueue(object : Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Timber.e(t.localizedMessage)
                restCallback.onFailure(t)
            }

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Timber.e(response.message())
                if (response.isSuccessful) {
                    restCallback.onSuccess(response)
                } else {
                    restCallback.onFailure(Throwable("Authentication failed! Please enter valid username and password.")) //TODO: change the hard coded string ...
                }
            }

        })
    }

}