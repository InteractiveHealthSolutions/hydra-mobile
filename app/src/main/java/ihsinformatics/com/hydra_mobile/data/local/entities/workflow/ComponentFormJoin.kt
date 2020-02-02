package ihsinformatics.com.hydra_mobile.data.local.entities.workflow

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity
//    (
//    tableName = "component_form_join",
//    primaryKeys = ["componentId", "formId"],
//    foreignKeys = [
//        ForeignKey(
//            entity = Component::class,
//            parentColumns = arrayOf("componentId"),
//            childColumns = arrayOf("componentId")
//        ), ForeignKey(
//            entity = Forms::class,
//            parentColumns = arrayOf("id"),
//            childColumns = arrayOf("formId")
//        )]
//)
class ComponentFormJoin(

    @PrimaryKey
    val id:Int,
    val workflowUUID: String,
    val phaseUUID: String,
    val componentId: Int,
    val formId: Int
) {
}