package ihsinformatics.com.hydra_mobile.data.local.dao

import androidx.room.*
import ihsinformatics.com.hydra_mobile.data.local.entities.Location
import ihsinformatics.com.hydra_mobile.data.local.entities.Obs

@Dao
interface ObsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertObs(obs: Obs)

    @Update
    fun updateObs(obs: Obs)

    @Delete
    fun deleteObs(obs: Obs)

    @Query("SELECT * FROM Obs WHERE id == :id")
    fun getObsById(id: Int): List<Obs>

    @Query("SELECT * FROM Obs")
    fun getAllObs(): List<Obs>
}