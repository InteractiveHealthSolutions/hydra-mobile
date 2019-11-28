package ihsinformatics.com.hydra_mobile.data.local.dao.workflow

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Component
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.PhasesComponentJoin


@Dao
interface PhaseComponentJoinDao {

    @Insert
    fun insert(phasesComponentJoin: PhasesComponentJoin)


    @Query(
        """
           SELECT * FROM Component
           INNER JOIN phase_component_join
           ON Component.componentId=phase_component_join.componentId
           WHERE phase_component_join.phaseId=:phaseId
           """
    )
    fun getComponentsByPhaseID(phaseId: Int): List<Component>


    @Update
    fun updatePhasesComponentJoin(phasesComponentJoin: PhasesComponentJoin)


}