package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.PrimaryKey

class PatientIdentifierType(
    val name: String,
    val description: String,
    val format: String,
    val checkDigit: Int

)  {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}