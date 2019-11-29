package ihsinformatics.com.hydra_mobile.data.local.dao.workflow

import androidx.lifecycle.LiveData
import androidx.room.*
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.WorkflowPhasesMap


@Dao
interface WorkflowPhasesMapDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWorkflowPhases(workflowPhasesMap: WorkflowPhasesMap)

    @Update
    fun updateWorkflowPhases(workflowPhasesMap: WorkflowPhasesMap)

    //@Delete
   // fun deleteWorkflowPhases(workflowPhasesMap: WorkflowPhasesMap)

    @Query("SELECT * FROM `WorkflowPhasesMap` WHERE id == :id")
    fun getWorkflowPhasesById(id: Int): List<WorkflowPhasesMap>

    @Query("SELECT * FROM `WorkflowPhasesMap`")
    fun getAllWorkflowPhasesWithLiveData(): LiveData<List<WorkflowPhasesMap>>

    @Query("DELETE FROM `WorkflowPhasesMap` ")
    fun deleteAllWorkflowPhases()


    @Query("SELECT * FROM `WorkflowPhasesMap`")
    fun getAllWorkflowPhases(): List<WorkflowPhasesMap>


    @Query("SELECT * FROM `WorkflowPhasesMap` WHERE workflowName == :name")
    fun getWorkflowPhasesByName(name: String): List<WorkflowPhasesMap>


}