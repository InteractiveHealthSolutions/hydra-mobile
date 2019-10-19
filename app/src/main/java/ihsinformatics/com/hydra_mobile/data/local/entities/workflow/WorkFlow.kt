package ihsinformatics.com.hydra_mobile.data.local.entities.workflow

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class WorkFlow(

    @PrimaryKey
    val id: Int,
    val name: String
)