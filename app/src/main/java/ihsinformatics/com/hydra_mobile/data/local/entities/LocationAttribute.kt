package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class LocationAttribute(
    val value: String,
    val uuid: String,
    val locationId: Long,
    val location: Location,
    val locationAttributeTypeId: Long,
    val locationAttributeType: LocationAttributeType
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}