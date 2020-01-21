package ihsinformatics.com.hydra_mobile.data.local.dao.commonLab

import androidx.lifecycle.LiveData
import androidx.room.*
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Forms
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestAllType
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestType
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.FormApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.FormResultApiResponse

@Dao
interface LabTestTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(labTestType: LabTestAllType)

    @Query("SELECT * FROM `LabTestAllType`")
    fun getAllLabTestTypes():List<LabTestAllType>

    @Query("DELETE FROM `LabTestAllType` ")
    fun deleteAllLabTestType()

}