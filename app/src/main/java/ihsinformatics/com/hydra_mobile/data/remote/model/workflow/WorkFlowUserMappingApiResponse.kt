package ihsinformatics.com.hydra_mobile.data.remote.model.workflow


import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.WorkFlow
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.WorkFlowWithUserMapping

class WorkFlowUserMappingApiResponse {

    @SerializedName("results")
    val workflowMapping: List<WorkFlowWithUserMapping>

    constructor(workflowMapping: List<WorkFlowWithUserMapping>) {
        this.workflowMapping = workflowMapping
    }

    constructor() {
        this.workflowMapping = ArrayList()
    }


}