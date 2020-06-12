package ihsinformatics.com.hydra_mobile.data.local.dao.workflow

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.ComponentForm
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.ComponentFormJoin
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Forms


@Dao
interface ComponentFormJoinDao {

    @Insert
    fun insert(componentFormJoin: ComponentFormJoin)

//    @Query(
//        """
//           SELECT * FROM Forms
//           INNER JOIN componentformjoin
//           ON Forms.id=componentformjoin.formId
//           WHERE componentformjoin.componentUUID=:uuid
//           """
//    )
//    fun getFormsByOtherJoins(uuid: String): List<Forms>



    @Query("DELETE FROM `ComponentFormJoin` ")
    fun deleteAllComponentForms()

    @Query("SELECT * from ComponentFormJoin")
    fun getAllJoins():List<ComponentFormJoin>

    @Query("SELECT * from ComponentFormJoin where componentUUID==:component AND phaseUUID==:phase AND workflowUUID==:workflow")
    fun getFormsByOtherJoins(component:String ,phase:String,workflow:String):List<ComponentFormJoin>

}