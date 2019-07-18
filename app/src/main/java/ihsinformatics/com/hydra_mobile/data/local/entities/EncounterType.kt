package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class EncounterType(
    val typeName: String,
    val uuid: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}