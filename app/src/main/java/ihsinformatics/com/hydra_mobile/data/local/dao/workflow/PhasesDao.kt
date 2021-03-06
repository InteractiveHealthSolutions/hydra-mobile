package ihsinformatics.com.hydra_mobile.data.local.dao.workflow

import androidx.lifecycle.LiveData
import androidx.room.*
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Phases

@Dao
interface PhasesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhase(phases: Phases)

    @Update
    fun updatePhase(phases: Phases)

    @Delete
    fun deletePhase(phases: Phases)

    @Query("DELETE FROM `Phases`")
    fun deleteAllPhases()


    @Query("SELECT * FROM `Phases` WHERE phaseId == :id")
    fun getPhaseById(id: Int): List<Phases>

    @Query("SELECT * FROM `Phases`")
    fun getAllPhases(): LiveData<List<Phases>>

    @Query("SELECT * FROM `Phases` WHERE name == :name")
    fun getPhaseUUIDByName(name:String):Phases
}