package ihsinformatics.com.hydra_mobile.data.remote.manager

import android.app.Application
import android.content.Context
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
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.PatientApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.user.UserResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkFlowApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.service.PatientApiService
import ihsinformatics.com.hydra_mobile.data.remote.service.WorkFlowApiService
import ihsinformatics.com.hydra_mobile.data.remote.service.WorkflowPhasesApiService


/**
 * @author  Shujaat ali
 * @Email   shujaat.ali@ihsinformatics.com
 * @version 1.0.0
 * @DateCreated   2019-5-14
 */

class RequestManager {

    lateinit var retrofit: Retrofit
    var okHttpClient: OkHttpClient? = null
    lateinit var gson: Gson

    constructor(context: Context, username: String, password: String) {

        initOkHttp(
            username,
            password
        )
        retrofit = Retrofit.Builder()
            .baseUrl("http://ihs.ihsinformatics.com:6811/openmrs/ws/rest/v1/")
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
        val userService = retrofit.create(UserApiService::class.java)

        userService.getUser(username, representation).enqueue(object : Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Timber.e(t.localizedMessage)
                restCallback.onFailure(t)
            }

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Timber.e(response.message())
                if (response.isSuccessful) {
                    restCallback.onSuccess(response.body())
                } else {
                    restCallback.onFailure(Throwable("Authentication failed! Please enter valid username and password.")) //TODO: change the hard coded string ...
                }
            }

        })
    }

    fun getWorkflowPhases(representation: String, restCallback: RESTCallback) {
        val workflowPhasesService = retrofit.create(WorkflowPhasesApiService::class.java)
        workflowPhasesService.getWorkflowPhases(representation).enqueue(object : Callback<WorkflowPhasesApiResponse> {

            override fun onResponse(
                call: Call<WorkflowPhasesApiResponse>,
                response: Response<WorkflowPhasesApiResponse>
            ) {
                Timber.e(response.message())

                if (response.isSuccessful) {
                    restCallback.onSuccess(response.body())
                } else {
                    restCallback.onFailure(Throwable("Not responding"))
                }
            }

            override fun onFailure(call: Call<WorkflowPhasesApiResponse>, t: Throwable) {

                Timber.e(t.localizedMessage)
                restCallback.onFailure(t)
            }

        })
    }




    fun getWorkflow(representation: String, restCallback: RESTCallback) {
        val workflowService = retrofit.create(WorkFlowApiService::class.java)
        workflowService.getWorkFlow(representation).enqueue(object : Callback<WorkFlowApiResponse> {

            override fun onResponse(
                call: Call<WorkFlowApiResponse>,
                response: Response<WorkFlowApiResponse>
            ) {
                Timber.e(response.message())

                if (response.isSuccessful) {
                    restCallback.onSuccess(response.body())
                } else {
                    restCallback.onFailure(Throwable("Not responding"))
                }
            }

            override fun onFailure(call: Call<WorkFlowApiResponse>, t: Throwable) {

                Timber.e(t.localizedMessage)
                restCallback.onFailure(t)
            }

        })
    }



    fun searchPatient(representation: String, searchQuery: String, restCallback: RESTCallback) {
        val patientSearch = retrofit.create(PatientApiService::class.java)

        patientSearch.getPatientByQuery(searchQuery, representation).enqueue(object : Callback<PatientApiResponse> {
            override fun onResponse(call: Call<PatientApiResponse>, response: Response<PatientApiResponse>) {
                Timber.e(response.message())
                if (response.isSuccessful) {
                    restCallback.onSuccess(response.body())
                } else {
                    restCallback.onFailure(Throwable("Not responding"))
                }

            }

            override fun onFailure(call: Call<PatientApiResponse>, t: Throwable) {
                Timber.e(t.localizedMessage)
                restCallback.onFailure(t)
            }

        })
    }

}