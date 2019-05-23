package ihsinformatics.com.hydra_mobile.data.local.dao

import androidx.room.*
import ihsinformatics.com.hydra_mobile.data.local.entities.AppSetting

@Dao
interface AppSettingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSetting(setting: AppSetting)

    @Update
    fun updateSetting(setting: AppSetting)

    @Delete
    fun deleteSetting(setting: AppSetting)

    @Query("SELECT * FROM AppSetting WHERE ip == :ip")
    fun getSettingByName(ip: String): List<AppSetting>

    @Query("SELECT * FROM AppSetting")
    fun getAllSetting(): List<AppSetting>

}