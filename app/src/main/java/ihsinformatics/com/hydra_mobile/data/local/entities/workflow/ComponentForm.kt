package ihsinformatics.com.hydra_mobile.data.local.entities.workflow

import androidx.room.Embedded
import androidx.room.Relation

class ComponentForm {
    @Embedded
    lateinit var component: Component
    @Relation(parentColumn = "id",
        entityColumn = "componentId")
    lateinit var  formList:List<Forms>
}