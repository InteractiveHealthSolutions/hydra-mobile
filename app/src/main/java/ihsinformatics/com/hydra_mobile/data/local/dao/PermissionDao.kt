package ihsinformatics.com.hydra_mobile.data.local.dao

import androidx.room.*
import ihsinformatics.com.hydra_mobile.data.local.entities.Permission

@Dao
interface PermissionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPermission(permission: Permission)

    @Update
    fun updatePermission(permission: Permission)

    @Delete
    fun deletePermission(permission: Permission)

    @Query("SELECT * FROM Permission WHERE id == :id")
    fun getPermissionById(id: Int): List<Permission>

    @Query("SELECT * FROM Permission ")
    fun getAllPermission(): List<Permission>

}