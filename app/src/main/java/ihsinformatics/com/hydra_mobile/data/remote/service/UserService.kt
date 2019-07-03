package ihsinformatics.com.hydra_mobile.data.remote.service



import ihsinformatics.com.hydra_mobile.data.remote.model.user.UserResponse
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.GET

/**
 * @author  Shujaat ali
 * @Email   shujaat.ali@ihsinformatics.com
 * @version 1.0.0
 * @DateCreated   2019-5-14
 */
interface UserService {

    @GET("user")
    fun getUser(@Query("q") username: String, @Query("v") representation: String
    ): Call<UserResponse>

}