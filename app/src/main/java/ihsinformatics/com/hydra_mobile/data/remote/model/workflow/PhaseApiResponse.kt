package ihsinformatics.com.hydra_mobile.data.remote.model.workflow


import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Phases
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.WorkFlow

class PhaseApiResponse {

    @SerializedName("phases")
    val phase: List<Phases>

    constructor(phase: List<Phases>) {
        this.phase = phase
    }

    constructor() {
        this.phase = ArrayList()
    }


}