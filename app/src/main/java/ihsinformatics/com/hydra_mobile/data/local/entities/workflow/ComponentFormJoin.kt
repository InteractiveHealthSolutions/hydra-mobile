package ihsinformatics.com.hydra_mobile.data.local.entities.workflow

import androidx.room.Entity
import androidx.room.ForeignKey


@Entity(
    tableName = "component_form_join",
    primaryKeys = ["comId", "formId"],
    foreignKeys = [
        ForeignKey(
            entity = Component::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("comId")
        ), ForeignKey(
            entity = Forms::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("formId")
        )]
)
class ComponentFormJoin(
    val comId: Int,
    val formId: Int
) {
}