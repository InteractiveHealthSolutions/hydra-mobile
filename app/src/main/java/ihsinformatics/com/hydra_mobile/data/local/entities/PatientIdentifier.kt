package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class PatientIdentifier(

    val identifier: String,
    val isPrimary: Boolean,
    val identifierTypeName: Long,
    val identifierType: IdentifierType,
    val patientId: Long,
    val patient: Patient

) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}