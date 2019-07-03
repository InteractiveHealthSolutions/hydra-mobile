package ihsinformatics.com.hydra_mobile.data.local.entities

import androidx.room.PrimaryKey
import java.util.*

class Role(
    val role: String,
    val description: String,
    val uuid: String

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}