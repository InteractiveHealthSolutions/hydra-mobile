package ihsinformatics.com.hydra_mobile.data.local.entities.workflow

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class WorkFlow(

    @SerializedName("uuid")
    val uuid: String,
    @PrimaryKey
    @SerializedName("workflowId")
    val workflowId: Int,
    @SerializedName("name")
    val name: String
)