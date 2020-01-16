package ihsinformatics.com.hydra_mobile.data.local.dao.commonLab

import androidx.lifecycle.LiveData
import androidx.room.*
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Forms
import ihsinformatics.com.hydra_mobile.data.remote.model.commonLab.LabTestType
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.FormApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.FormResultApiResponse

@Dao
interface LabTestTypeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(labTestType: LabTestType)

    @Query("SELECT * FROM `LabTestType`")
    fun getAllLabTestTypes():List<LabTestType>

    @Query("DELETE FROM `LabTestType` ")
    fun deleteAllLabTestType()

}