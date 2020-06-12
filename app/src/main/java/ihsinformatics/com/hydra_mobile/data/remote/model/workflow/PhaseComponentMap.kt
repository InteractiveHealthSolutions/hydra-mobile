package ihsinformatics.com.hydra_mobile.data.remote.model.workflow


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class PhaseComponentMap(

    @SerializedName("displayOrder")
    val displayOrder: Int,

    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("uuid")
    val uuid: String,

    @SerializedName("phaseUUID")
    val phaseUUID: String,

    @SerializedName("workflowUUID")
    val workflowUUID: String,

    @SerializedName("componentUUID")
    val componentUUID: String

)