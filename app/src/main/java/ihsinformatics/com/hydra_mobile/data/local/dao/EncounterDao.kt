package ihsinformatics.com.hydra_mobile.data.local.dao

import androidx.room.*
import ihsinformatics.com.hydra_mobile.data.local.entities.Encounter

@Dao
interface EncounterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEncounter(encounter: Encounter)

    @Update
    fun updateEncounter(encounter: Encounter)

    @Delete
    fun deleteEncounter(encounter: Encounter)

    @Query("SELECT * FROM Encounter WHERE id == :id")
    fun getEncounterById(id: Int): List<Encounter>

    @Query("SELECT * FROM Encounter")
    fun getAllEncounter(): List<Encounter>
}