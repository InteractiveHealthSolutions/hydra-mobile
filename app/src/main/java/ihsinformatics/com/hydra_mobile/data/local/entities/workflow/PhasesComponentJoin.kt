package ihsinformatics.com.hydra_mobile.data.local.entities.workflow

import androidx.room.Entity
import androidx.room.ForeignKey


@Entity(
    tableName = "phase_component_join",
    primaryKeys = ["phaseId", "componentId"],
    foreignKeys = [
        ForeignKey(
            entity = Phases::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("phaseId")
        ), ForeignKey(
            entity = Component::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("componentId")
        )]
)
data class PhasesComponentJoin(
    val phaseId: Int,
    val componentId: Int
)

