package ihsinformatics.com.hydra_mobile.persistentdata.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ihsinformatics.com.hydra_mobile.persistentdata.entities.AppSetting
import io.reactivex.Observable

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
    fun getAllSetting(): Observable<List<AppSetting>>

}