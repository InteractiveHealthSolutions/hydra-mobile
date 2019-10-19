package ihsinformatics.com.hydra_mobile.data.local.entities.workflow

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Phases(
    val name: String,
    @PrimaryKey
    val id: Int
)