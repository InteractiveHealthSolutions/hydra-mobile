package ihsinformatics.com.hydra_mobile.data.remote.manager

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.ihsinformatics.dynamicformsgenerator.wrapper.ToastyWidget
import ihsinformatics.com.hydra_mobile.HydraApp.Companion.context
import ihsinformatics.com.hydra_mobile.common.Constant
import ihsinformatics.com.hydra_mobile.data.remote.model.BasicAuthInterceptor
import ihsinformatics.com.hydra_mobile.data.remote.model.RESTCallback
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.LabTestTypeApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.LocationApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.SystemSettingApiResponse
import ihsinformatics.com.hydra_mobile.utils.AppConfiguration
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.PatientApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.user.UserResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.*
import ihsinformatics.com.hydra_mobile.data.remote.service.*
import ihsinformatics.com.hydra_mobile.ui.activity.LoginActivity
import ihsinformatics.com.hydra_mobile.ui.viewmodel.WorkFlowViewModel
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


class RequestManager {

    lateinit var retrofit: Retrofit
    lateinit var retrofitForFormAPI: Retrofit   //TODO remove this retrofit instance ~Taha
    lateinit var retrofitTestOrder: Retrofit


    var okHttpClient: OkHttpClient? = null

    var okHttpClientPatientList: OkHttpClient? = null
    var okHttpClientForm: OkHttpClient? = null
    // lateinit var gson: Gson
    lateinit var context: Context

    constructor(context: Context, username: String, password: String) {

        this.context = context

        initOkHttp(username, password)

        initOkHttpPatientList(username, password)

        initOkHttpForm(username, password)

        retrofit = Retrofit.Builder().baseUrl(getBaseUrl(context)).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()

        retrofitForFormAPI = Retrofit.Builder().baseUrl(getBaseUrl(context)).addConverterFactory(GsonConverterFactory.create()).client(okHttpClientForm).build()

        retrofitTestOrder = Retrofit.Builder().baseUrl(getBaseUrl(context)).addConverterFactory(GsonConverterFactory.create()).client(okHttpClientPatientList).build()


    }

    fun getFormRetrofit(): Retrofit {
        return retrofitForFormAPI
    }

    private fun initOkHttp(username: String, password: String) {
        val httpClient = OkHttpClient().newBuilder()
        httpClient.addInterceptor(BasicAuthInterceptor(username, password))
        httpClient.connectTimeout(100000, TimeUnit.SECONDS).readTimeout(10000, TimeUnit.SECONDS).build()

        okHttpClient = httpClient.build()
    }


    private fun initOkHttpPatientList(username: String, password: String) {
        val httpClient = OkHttpClient().newBuilder()
        httpClient.addInterceptor(BasicAuthInterceptor(username, password))
        httpClient.connectTimeout(100000, TimeUnit.SECONDS).readTimeout(100, TimeUnit.SECONDS).build()

        okHttpClientPatientList = httpClient.build()
    }

    private fun initOkHttpForm(username: String, password: String) {
        val httpClient = OkHttpClient().newBuilder()
        httpClient.addInterceptor(BasicAuthInterceptor(username, password))
        httpClient.connectTimeout(100000, TimeUnit.SECONDS).readTimeout(100, TimeUnit.SECONDS).build()

        okHttpClientForm = httpClient.build()
    }

    fun getPatientRetrofit(): Retrofit {
        return retrofitTestOrder
    }

    private fun getBaseUrl(context: Context): String {
        return AppConfiguration().getBaseUrl(context)
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
                if (response.isSuccessful && response.body()!!.userList.size > 0 && null != response.body()!!.userList[0].username && !response.body()!!.userList[0].username.equals("")) {
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
                call: Call<WorkflowPhasesApiResponse>, response: Response<WorkflowPhasesApiResponse>
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


    fun getPhaseComponentMap(representation: String, restCallback: RESTCallback) {
        val phaseComponentService = retrofit.create(PhaseComponentApiService::class.java)
        phaseComponentService.getPhaseComponent(representation).enqueue(object : Callback<PhaseComponentApiResponse> {

            override fun onResponse(
                call: Call<PhaseComponentApiResponse>, response: Response<PhaseComponentApiResponse>
                                   ) {
                Timber.e(response.message())

                if (response.isSuccessful) {
                    restCallback.onSuccess(response.body())
                } else {
                    restCallback.onFailure(Throwable("Not responding"))
                }
            }

            override fun onFailure(call: Call<PhaseComponentApiResponse>, t: Throwable) {

                Timber.e(t.localizedMessage)
                restCallback.onFailure(t)
            }

        })
    }


    fun getWorkflow(representation: String, restCallback: RESTCallback) {
        val workflowService = retrofit.create(WorkFlowApiService::class.java)
        workflowService.getWorkFlow(representation).enqueue(object : Callback<WorkFlowApiResponse> {

            override fun onResponse(
                call: Call<WorkFlowApiResponse>, response: Response<WorkFlowApiResponse>
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


    fun getPhases(representation: String, restCallback: RESTCallback) {
        val phaseService = retrofit.create(PhaseApiService::class.java)
        phaseService.getPhase(representation).enqueue(object : Callback<PhaseApiResponse> {

            override fun onResponse(
                call: Call<PhaseApiResponse>, response: Response<PhaseApiResponse>
                                   ) {
                Timber.e(response.message())

                if (response.isSuccessful) {
                    restCallback.onSuccess(response.body())
                } else {
                    restCallback.onFailure(Throwable("Not responding"))
                }
            }

            override fun onFailure(call: Call<PhaseApiResponse>, t: Throwable) {

                Timber.e(t.localizedMessage)
                restCallback.onFailure(t)
            }

        })
    }


    fun getComponents(representation: String, restCallback: RESTCallback) {
        val componentService = retrofit.create(ComponentApiService::class.java)
        componentService.getComponents().enqueue(object : Callback<ComponentApiResponse> {

            override fun onResponse(
                call: Call<ComponentApiResponse>, response: Response<ComponentApiResponse>
                                   ) {
                Timber.e(response.message())

                if (response.isSuccessful) {
                    restCallback.onSuccess(response.body())
                } else {
                    restCallback.onFailure(Throwable("Not responding"))
                }
            }

            override fun onFailure(call: Call<ComponentApiResponse>, t: Throwable) {

                Timber.e(t.localizedMessage)
                restCallback.onFailure(t)
            }

        })
    }


    fun getForms(representation: String, restCallback: RESTCallback) {
        val formService = retrofit.create(FormApiService::class.java)
        formService.getForms().enqueue(object : Callback<FormApiResponse> {

            override fun onResponse(
                call: Call<FormApiResponse>, response: Response<FormApiResponse>
                                   ) {
                Timber.e(response.message())

                if (response.isSuccessful) {
                    restCallback.onSuccess(response.body())
                } else {
                    restCallback.onFailure(Throwable("Not responding"))
                }
            }

            override fun onFailure(call: Call<FormApiResponse>, t: Throwable) {

                Timber.e(t.localizedMessage)
                restCallback.onFailure(t)
            }

        })
    }


    fun getFormsResult(restCallback: RESTCallback) {
        val formService = retrofit.create(FormResultApiService::class.java)
        formService.getFormsResult().enqueue(object : Callback<String> {

            override fun onResponse(
                call: Call<String>, response: Response<String>
                                   ) {
                Timber.e(response.message())

                if (response.isSuccessful) {
                    restCallback.onSuccess(response.body())
                } else {
                    restCallback.onFailure(Throwable("Not responding"))
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

                Timber.e(t.localizedMessage)
                restCallback.onFailure(t)
            }

        })
    }


    fun getLabTestTypeResult(representation: String, restCallback: RESTCallback) {
        val labTestType = retrofit.create(LabTestTypeApiService::class.java)
        labTestType.getLabTestType(representation).enqueue(object : Callback<LabTestTypeApiResponse> {

            override fun onResponse(
                call: Call<LabTestTypeApiResponse>, response: Response<LabTestTypeApiResponse>
                                   ) {
                Timber.e(response.message())

                if (response.isSuccessful) {
                    restCallback.onSuccess(response.body())
                } else {
                    restCallback.onFailure(Throwable("Not responding"))
                }
            }

            override fun onFailure(call: Call<LabTestTypeApiResponse>, t: Throwable) {

                Timber.e(t.localizedMessage)
                restCallback.onFailure(t)
            }

        })
    }


    fun getLocation(representation: String, restCallback: RESTCallback) {
        val location = retrofit.create(LocationApiService::class.java)
        location.getLocation(representation).enqueue(object : Callback<LocationApiResponse> {

            override fun onResponse(
                call: Call<LocationApiResponse>, response: Response<LocationApiResponse>
                                   ) {
                Timber.e(response.message())

                if (response.isSuccessful) {
                    restCallback.onSuccess(response.body())

                } else {
                    restCallback.onFailure(Throwable("Not responding"))
                }
            }

            override fun onFailure(call: Call<LocationApiResponse>, t: Throwable) {

                Timber.e(t.localizedMessage)
                restCallback.onFailure(t)
            }

        })
    }


    fun getLocationAndCurrency(queryString: String, representation: String, restCallback: RESTCallback) {
        val location = retrofit.create(LocationApiService::class.java)
        location.getLocationAndCurrency(queryString, representation).enqueue(object : Callback<SystemSettingApiResponse> {

            override fun onResponse(
                call: Call<SystemSettingApiResponse>, response: Response<SystemSettingApiResponse>
                                   ) {
                Timber.e(response.message())

                if (response.isSuccessful) {
                    restCallback.onSuccess(response.body())

                } else {
                    restCallback.onFailure(Throwable("Not responding"))
                }
            }

            override fun onFailure(call: Call<SystemSettingApiResponse>, t: Throwable) {

                Timber.e(t.localizedMessage)
                restCallback.onFailure(t)
            }

        })
    }

    fun searchPatient(representation: String, searchQuery: String, restCallback: RESTCallback) {
        val patientSearch = retrofit.create(PatientApiService::class.java)

        patientSearch.getPatientByQuery(searchQuery, Constant.REPRESENTATION).enqueue(object : Callback<PatientApiResponse> {
            override fun onResponse(
                call: Call<PatientApiResponse>, response: Response<PatientApiResponse>
                                   ) {
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


//    fun patientListByIdentifier(searchQuery: String) {
//        val patientSearch = retrofit.create(PatientApiService::class.java)
//        lateinit var patientList: List<Patient>
//        patientSearch.getPatientByIdentifier(searchQuery)
//            .enqueue(object : Callback<PatientApiResponse> {
//                override fun onResponse(
//                    call: Call<PatientApiResponse>,
//                    response: Response<PatientApiResponse>
//                ) {
//                    Timber.e(response.message())
//                    if (response.isSuccessful) {
//                        patientList = response.body()!!.patient
//                    } else {
//                        patientList = ArrayList<Patient>()
//                    }
//
//                }
//
//                override fun onFailure(call: Call<PatientApiResponse>, t: Throwable) {
//                    Timber.e(t.localizedMessage)
//                    patientList = ArrayList<Patient>()
//                }
//
//            })
//
//    }

}