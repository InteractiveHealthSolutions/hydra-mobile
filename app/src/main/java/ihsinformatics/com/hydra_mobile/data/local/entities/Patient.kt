package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Patient(
    val patientId: String,
    val givenName: String,
    val familyName:String,
    val age: Int,
    val gender: String,
    val contactNumber: String,
    val externalId: String,
    val enrs: String,
    val districtTBNumber: String,
    val endTBId: String,
   // val person: Person,
    val pid: Int,
    val voided:Boolean

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}