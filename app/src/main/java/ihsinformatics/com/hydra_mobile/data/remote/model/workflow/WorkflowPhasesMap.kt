package ihsinformatics.com.hydra_mobile.data.remote.model.workflow


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class WorkflowPhasesMap(
    @SerializedName("displayOrder")
    val displayOrder: Int,
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("phaseName")
    val phaseName: String,
    @SerializedName("phaseUUID")
    val phaseUUID: String,
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("workflowName")
    val workflowName: String,
    @SerializedName("workflowUUID")
    val workflowUUID: String
)