package ihsinformatics.com.hydra_mobile.data.local.dao.workflow

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.ComponentFormJoin
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Forms


@Dao
interface ComponentFormJoinDao {

    @Insert
    fun insert(componentFormJoin: ComponentFormJoin)

    @Query(
        """
           SELECT * FROM Forms
           INNER JOIN component_form_join
           ON Forms.id=component_form_join.formId
           WHERE component_form_join.componentId=:componentId
           """
    )
    fun getComponentFormList(componentId: Int): List<Forms>

    @Query("DELETE FROM `component_form_join` ")
    fun deleteAllComponentForms()
}