package ihsinformatics.com.hydra_mobile.data.local.dao.workflow

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.ComponentForm


@Dao
interface ComponentFormDao {

    @Query("SELECT * from Component")
    fun getComponentFormList(): LiveData<List<ComponentForm>>

}