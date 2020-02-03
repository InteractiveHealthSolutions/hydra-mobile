package ihsinformatics.com.hydra_mobile.data.local.dao.workflow

import androidx.lifecycle.LiveData
import androidx.room.*
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.PhaseComponentMap
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesMap


@Dao
interface PhaseComponentMapDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhaseComponent(phaseComponentMap: PhaseComponentMap)

    @Update
    fun updatePhaseComponent(phaseComponentMap: PhaseComponentMap)

    //@Delete
   // fun deletePhaseComponent(phaseComponentMap: PhaseComponentMap)

    @Query("SELECT * FROM `PhaseComponentMap` WHERE id == :id")
    fun getPhaseComponentById(id: Int): List<PhaseComponentMap>

    @Query("SELECT * FROM `PhaseComponentMap`")
    fun getAllPhaseComponentWithLiveData(): LiveData<List<PhaseComponentMap>>

    @Query("DELETE FROM `PhaseComponentMap` ")
    fun deleteAllPhaseComponent()


    @Query("SELECT * FROM `PhaseComponentMap`")
    fun getAllPhaseComponent(): List<PhaseComponentMap>


    @Query("SELECT * FROM `PhaseComponentMap` WHERE phaseUUID == :phase AND workflowUUID==:workflow")
    fun getComponentsByPhaseandWorkflow(phase: String,workflow:String): List<PhaseComponentMap>

    @Query("SELECT * FROM `PhaseComponentMap` WHERE  workflowUUID==:workflow")
    fun getComponentsByWorkflow(workflow:String): List<PhaseComponentMap>

}