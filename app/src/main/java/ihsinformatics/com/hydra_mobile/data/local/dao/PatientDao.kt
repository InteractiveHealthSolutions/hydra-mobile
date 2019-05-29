package ihsinformatics.com.hydra_mobile.data.local.dao

import androidx.room.*
import ihsinformatics.com.hydra_mobile.data.local.entities.Patient

@Dao
interface PatientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPatient(patient: Patient)

    @Update
    fun updatePatient(patient: Patient)

    @Delete
    fun deletePatient(patient: Patient)

    @Query("SELECT * FROM Patient WHERE id == :id")
    fun getPatientById(id: Int): List<Patient>

    @Query("SELECT * FROM Patient ")
    fun getAllPatient(): List<Patient>
}