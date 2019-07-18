package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class IdentifierType(
    val identifierTypeName: String,
    val uuid: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}