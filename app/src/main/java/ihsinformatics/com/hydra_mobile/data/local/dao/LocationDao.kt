package ihsinformatics.com.hydra_mobile.data.local.dao

import androidx.room.*
import ihsinformatics.com.hydra_mobile.data.local.entities.Location


@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(location: Location)

    @Update
    fun updateLocation(location: Location)

    @Delete
    fun deleteLocation(location: Location)

    @Query("SELECT * FROM Location WHERE id == :id")
    fun getLocationById(id: Int): List<Location>

    @Query("SELECT * FROM Location")
    fun getAllLocation(): List<Location>
}