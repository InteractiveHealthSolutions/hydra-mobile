package ihsinformatics.com.hydra_mobile.data.remote.service

import ihsinformatics.com.hydra_mobile.data.remote.APIResponses.*
import ihsinformatics.com.hydra_mobile.data.remote.model.FormSubmissionReqBody
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.*
import ihsinformatics.com.hydra_mobile.data.remote.model.formSubmission
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface FormSubmissionApiService {


    @Headers("Accept: application/json")
    @POST("hydra/formSubmission")
    fun submitForm(@Body body: FormSubmissionReqBody):Call<formSubmission>
}

