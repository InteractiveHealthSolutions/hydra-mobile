package ihsinformatics.com.hydra_mobile.data.local.entities.workflow


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ihsinformatics.dynamicformsgenerator.data.core.questions.Question

@Entity
class Forms(
    @PrimaryKey
    val id: Int,
    val name: String,
    val workflowUUID: String,
    val phaseUUID: String,
    val componentUUID: String,
    val formUUID: String,
    val componentId: Int,
    val componentFormId: Int,
    val componentFormUUID: String,
    val encounterType: String,
    val component: String,
    val phase: String,
    val workflow: String,
    val questions:String

)