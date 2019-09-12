package ihsinformatics.com.hydra_mobile.data.local.dao.workflow

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.PhasesComponent

@Dao
interface PhaseComponentDao {

    @Query("SELECT * from Phases")
    fun getPhaseComponentList(): LiveData<List<PhasesComponent>>

}