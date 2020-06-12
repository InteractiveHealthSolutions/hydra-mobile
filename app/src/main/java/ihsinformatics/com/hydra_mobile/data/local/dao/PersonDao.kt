package ihsinformatics.com.hydra_mobile.data.local.dao

import androidx.room.*
import ihsinformatics.com.hydra_mobile.data.local.entities.Person

@Dao
interface PersonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPerson(person: Person)

    @Update
    fun updatePerson(person: Person)

    @Delete
    fun deletePerson(person: Person)

    @Query("SELECT * FROM Person WHERE id == :id")
    fun getPersonById(id: Int): List<Person>

    @Query("SELECT * FROM Person ")
    fun getAllPerson(): List<Person>

}