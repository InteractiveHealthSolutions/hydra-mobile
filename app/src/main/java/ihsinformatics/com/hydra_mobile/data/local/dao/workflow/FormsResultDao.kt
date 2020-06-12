package ihsinformatics.com.hydra_mobile.data.local.dao.workflow

import androidx.lifecycle.LiveData
import androidx.room.*
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Forms
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.FormApiResponse
import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.FormResultApiResponse

@Dao
interface FormsResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFormResult(formsResult: FormResultApiResponse)

    @Query("SELECT * FROM `FormResultApiResponse`")
    fun getFormResults():FormResultApiResponse

    @Query("DELETE FROM `FormResultApiResponse` ")
    fun deleteFormResult()

}