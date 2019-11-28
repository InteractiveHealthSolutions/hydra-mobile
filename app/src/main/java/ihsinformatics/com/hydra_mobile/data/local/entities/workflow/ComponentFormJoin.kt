package ihsinformatics.com.hydra_mobile.data.local.entities.workflow

import androidx.room.Entity
import androidx.room.ForeignKey


@Entity(
    tableName = "component_form_join",
    primaryKeys = ["componentId", "formId"],
    foreignKeys = [
        ForeignKey(
            entity = Component::class,
            parentColumns = arrayOf("componentId"),
            childColumns = arrayOf("componentId")
        ), ForeignKey(
            entity = Forms::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("formId")
        )]
)
class ComponentFormJoin(
    val componentId: Int,
    val formId: Int
) {
}