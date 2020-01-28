package ihsinformatics.com.hydra_mobile.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ihsinformatics.com.hydra_mobile.data.remote.model.patient.Patient

@Dao
interface PatientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPatient(patient: Patient)

    @Update
    fun updatePatient(patient: Patient)

    @Delete
    fun deletePatient(patient: Patient)

    @Query("SELECT * FROM Patient WHERE patientUUID == :uuid")
    fun getPatientById(uuid: String): LiveData<List<Patient>>

    @Query("SELECT * FROM Patient ")
    fun getAllPatient(): List<Patient>

    @Query("SELECT * FROM Patient WHERE identifier == :id")
    fun getPatientByID(id: String): List<Patient>
}