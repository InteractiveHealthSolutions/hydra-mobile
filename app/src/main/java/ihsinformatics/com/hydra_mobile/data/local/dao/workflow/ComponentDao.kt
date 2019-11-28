package ihsinformatics.com.hydra_mobile.data.local.dao.workflow

import androidx.lifecycle.LiveData
import androidx.room.*
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Component


@Dao
interface ComponentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComponent(component: Component)

    @Update
    fun updateComponent(component: Component)

    @Delete
    fun deleteComponent(component: Component)

    @Query("SELECT * FROM Component WHERE componentId == :id")
    fun getComponentById(id: Int): List<Component>

    @Query("SELECT * FROM Component")
    fun getAllComponent(): LiveData<List<Component>>

}