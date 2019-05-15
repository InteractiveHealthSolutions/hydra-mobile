package ihsinformatics.com.hydra_mobile.network.manager

import android.app.Application
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import ihsinformatics.com.hydra_mobile.network.model.BasicAuthInterceptor
import ihsinformatics.com.hydra_mobile.network.model.RESTCallback
import ihsinformatics.com.hydra_mobile.network.model.user.UserResponse
import ihsinformatics.com.hydra_mobile.network.service.UserService
import ihsinformatics.com.hydra_mobile.utils.AppConfiguration
import ihsinformatics.com.hydra_mobile.utils.Constant
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * @author  Shujaat ali
 * @Email   shujaat.ali@ihsinformatics.com
 * @version 1.0.0
 * @DateCreated   2019-5-14
 */

class RequestManager {


    companion object {

        var retrofit: Retrofit? = null
        var okHttpClient: OkHttpClient? = null

        fun getClient(application: Application, username: String, password: String): Retrofit? {

            if (okHttpClient == null)
                initOkHttp(
                    application,
                    username,
                    password
                )
            if (retrofit == null) {
                val baseUrl = getBaseUrl(application)
                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl) //"https://mrs.ghd.ihn.org.pk:443/openmrs/ws/rest/v1/"
                    .client(okHttpClient!!)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }

        private fun initOkHttp(application: Application, username: String, password: String) {
            val httpClient = OkHttpClient().newBuilder()
                .connectTimeout(Constant.REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(Constant.REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(Constant.REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)

            httpClient.addInterceptor(
                BasicAuthInterceptor(
                    username,
                    password
                )
            )

            okHttpClient = httpClient.build()
        }

        private fun getBaseUrl(application: Application): String {
            return AppConfiguration.getBaseUrl(application)
        }

        fun authenticateUser(username: String, representation: String, restCallback: RESTCallback) {
            val userService = retrofit!!.create(UserService::class.java)
            userService.getUser(username, representation).enqueue(object : Callback<List<UserResponse>> {
                override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                    Timber.e(t.localizedMessage)
                    restCallback.onFailure(t)
                }

                override fun onResponse(call: Call<List<UserResponse>>, response: Response<List<UserResponse>>) {
                    if (response.isSuccessful) {
                        restCallback.onSuccess(response)
                    } else {
                        restCallback.onFailure(Throwable("Authentication failed! Please enter valid username and password.")) //TODO: change the hard coded string ...
                    }
                }
            })

        }

    }


}