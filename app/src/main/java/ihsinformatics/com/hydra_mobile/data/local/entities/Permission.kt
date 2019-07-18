package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class Permission(
    val permissionName: String,
    val uuid: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}