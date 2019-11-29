package ihsinformatics.com.hydra_mobile.data.local.dao.workflow

import androidx.lifecycle.LiveData
import androidx.room.*
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.WorkFlow

@Dao
interface WorkFlowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWorkFlow(workFlow: WorkFlow)

    @Update
    fun updateWorkFlow(workFlow: WorkFlow)

    @Delete
    fun deleteWorkFlow(workFlow: WorkFlow)

    @Query("DELETE FROM `WorkFlow`")
    fun deleteAllWorkflow()

    @Query("SELECT * FROM `WorkFlow` WHERE workflowId == :id")
    fun getWorkFlowByID(id: Int): List<WorkFlow>

    @Query("SELECT * FROM `WorkFlow`")
    fun getAllWorkFlowsLiveData(): LiveData<List<WorkFlow>>

    @Query("SELECT * FROM WorkFlow")
    fun getAllWorkFlows(): LiveData<List<WorkFlow>>

}