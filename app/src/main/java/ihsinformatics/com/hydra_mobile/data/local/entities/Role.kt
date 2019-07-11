package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.PrimaryKey
import java.util.*

class Role(
    val role: String,
    val description: String

) {
    @PrimaryKey(autoGenerate = true)
    var roleId: Int = 0
}