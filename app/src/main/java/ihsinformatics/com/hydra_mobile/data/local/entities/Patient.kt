package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.PrimaryKey

class Patient(
    val patientId: String,
    val externalId: String,
    val enrs: String,
    val districtTBNumber: String,
    val endTBId: String,
    val person: Person,
    val pid: Int,
    val identifierLocation: String

) : AbstractModel() {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}