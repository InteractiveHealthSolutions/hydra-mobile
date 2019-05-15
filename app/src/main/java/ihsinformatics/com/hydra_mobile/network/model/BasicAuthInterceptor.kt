package ihsinformatics.com.hydra_mobile.network.model

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author  Shujaat ali
 * @Email   shujaat.ali@ihsinformatics.com
 * @version 1.0.0
 * @DateCreated   2019-5-14
 */

class BasicAuthInterceptor(username: String, password: String) : Interceptor {

    private val credentials: String = Credentials.basic(username, password)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", credentials).build()
        return chain.proceed(authenticatedRequest)
    }
}