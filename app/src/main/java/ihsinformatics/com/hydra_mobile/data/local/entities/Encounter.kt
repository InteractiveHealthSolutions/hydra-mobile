package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Encounter(
    val encounterDatetime: String,
    val dateCreated: String,
    val voided: String,
    val patientId: String,
   // val patient: Patient,
    val encounterTypeId:Long,
    //val encounterType:EncounterType,
    val locationId:Long,
   // val location: Location,
    val createdBy:Long,
   // val user: User,
    val isUploaded:Boolean

    //TODO:add the relationship

) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}