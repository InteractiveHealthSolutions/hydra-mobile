package ihsinformatics.com.hydra_mobile.data.local.entities.workflow

import androidx.room.Embedded
import androidx.room.Relation


class PhasesComponent {
    @Embedded
    lateinit var phases:Phases
    @Relation(parentColumn = "id",
        entityColumn = "phaseId")
    lateinit var  componentList:List<Component>
}