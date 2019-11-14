package ihsinformatics.com.hydra_mobile.data.remote.model.workflow


import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.WorkFlow

class WorkFlowApiResponse {

    @SerializedName("workflows")
    val workflow: List<WorkFlow>

    constructor(workflow: List<WorkFlow>) {
        this.workflow = workflow
    }

    constructor() {
        this.workflow = ArrayList()
    }


}