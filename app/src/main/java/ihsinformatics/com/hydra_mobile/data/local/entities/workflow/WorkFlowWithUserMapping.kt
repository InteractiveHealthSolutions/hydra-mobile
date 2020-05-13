package ihsinformatics.com.hydra_mobile.data.local.entities.workflow

import com.google.gson.annotations.SerializedName

data class WorkFlowWithUserMapping(

    @SerializedName("workflow")
    val workflow: WorkFlow
)