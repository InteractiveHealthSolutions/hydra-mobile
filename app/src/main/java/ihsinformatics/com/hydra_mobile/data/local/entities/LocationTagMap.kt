package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class LocationTagMap(

    val locationId: Long,
    val location: Location,
    val locationTagId: Long,
    val locationTag: LocationTag
   //Todo:relation mapping..
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}