package ihsinformatics.com.hydra_mobile.network.service



import ihsinformatics.com.hydra_mobile.network.model.user.UserResponse
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

    @Headers("Accept: application/json")
    @GET("user")
    fun getUser(
        @Query("q") username: String, @Query("v") representation: String
    ): Call<List<UserResponse>>

}