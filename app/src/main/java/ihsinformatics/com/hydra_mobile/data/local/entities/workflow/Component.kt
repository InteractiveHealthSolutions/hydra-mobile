package ihsinformatics.com.hydra_mobile.data.local.entities.workflow

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Component(
    val name: String,
    @PrimaryKey
    val id: Int,
    val phaseId: Int
) {
}