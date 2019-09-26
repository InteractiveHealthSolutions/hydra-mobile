package ihsinformatics.com.hydra_mobile.data.local.entities.workflow

import androidx.room.Entity
import androidx.room.ForeignKey


@Entity(
    tableName = "workflow_phase_join",
    primaryKeys = ["phaseId", "workflowId"],
    foreignKeys = [
        ForeignKey(
            entity = Phases::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("phaseId")
        ), ForeignKey(
            entity = Workflow::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("workflowId")
        )]
)
class WorkflowPhasesJoin(
    val phaseId: Int,
    val workflowId: Int
)
