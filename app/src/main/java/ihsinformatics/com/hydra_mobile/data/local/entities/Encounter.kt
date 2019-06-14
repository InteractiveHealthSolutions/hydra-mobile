package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Encounter(
    val encounterDatetime: String,
    val dateCreated: String,
    val voided: String,
    // val patientId: String,
    @Embedded(prefix = "patientId")
    val patient: Patient,
    //val encounterTypeId: Long,
    @Embedded(prefix = "encounterTypeId")
    val encounterType: EncounterType,
    //val locationId: Long,
    @Embedded(prefix = "locationId")
    val location: Location,
    //val createdBy: Long,
    @Embedded(prefix = "createdBy")
    val user: User,
    val isUploaded: Boolean

) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}