package ihsinformatics.com.hydra_mobile.data.local.dao

import androidx.room.*
import ihsinformatics.com.hydra_mobile.data.local.entities.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("DELETE FROM `User`")
    fun deleteAllUsers()

    @Query("SELECT * FROM `User` WHERE id == :id")
    fun getUserById(id: Int): List<User>

    @Query("SELECT * FROM `User`")
    fun getAllUser(): List<User>

    @Query("SELECT * FROM User WHERE password == :pass AND username == :user")
    fun getUserByUsernameAndPass(user:String ,pass:String):List<User>
}