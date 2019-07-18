package ihsinformatics.com.hydra_mobile.data.local.dao

import androidx.room.*
import ihsinformatics.com.hydra_mobile.data.local.entities.Address
import ihsinformatics.com.hydra_mobile.data.local.entities.Concept

@Dao
interface ConceptDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConcept(concept: Concept)

    @Update
    fun updateConcept(concept: Concept)

    @Delete
    fun deleteConcept(concept: Concept)

    @Query("SELECT * FROM Concept WHERE id == :id")
    fun getConceptById(id: Int): List<Concept>

    @Query("SELECT * FROM Concept")
    fun getAllConcept(): List<Concept>
}