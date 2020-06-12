package ihsinformatics.com.hydra_mobile.data.remote.model.workflow


import com.google.gson.annotations.SerializedName

class WorkflowPhasesApiResponse {

    @SerializedName("workflowPhasesMap")
    val workflowPhasesMap: List<WorkflowPhasesMap>

    constructor(workflowPhasesMap: List<WorkflowPhasesMap>) {
        this.workflowPhasesMap = workflowPhasesMap
    }

    constructor() {
        this.workflowPhasesMap = ArrayList()
    }


}