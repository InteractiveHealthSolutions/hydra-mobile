package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.PrimaryKey

class Encounter(
    val encounterType: String,
    val encounterDatetime: String,
    val encounterLocation: String,
    val patientId: String,
    val dateCreated: String,
    val creator: String,
    val voided: String
) : AbstractModel() {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}