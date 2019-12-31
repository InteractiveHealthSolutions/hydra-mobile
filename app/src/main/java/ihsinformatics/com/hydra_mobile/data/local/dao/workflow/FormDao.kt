package ihsinformatics.com.hydra_mobile.data.local.dao.workflow

import androidx.lifecycle.LiveData
import androidx.room.*
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Forms

@Dao
interface FormDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForm(forms: Forms)

    @Update
    fun updateForm(forms: Forms)

    @Delete
    fun deleteForm(forms: Forms)

    @Query("SELECT * FROM `Forms` WHERE id == :id")
    fun getFormById(id: Int): List<Forms>

    @Query("SELECT * FROM `Forms`")
    fun getAllLiveForms(): LiveData<List<Forms>>

    @Query("SELECT * FROM `Forms`")
    fun getAllForms(): List<Forms>

    @Query("DELETE FROM `Forms`")
    fun deleteAllForms()
}