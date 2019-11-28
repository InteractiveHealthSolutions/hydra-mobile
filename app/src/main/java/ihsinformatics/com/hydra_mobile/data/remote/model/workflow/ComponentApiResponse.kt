package ihsinformatics.com.hydra_mobile.data.remote.model.workflow


import com.google.gson.annotations.SerializedName
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Component
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Phases
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.WorkFlow

class ComponentApiResponse {

    @SerializedName("components")
    val component: List<Component>

    constructor(component: List<Component>) {
        this.component = component
    }

    constructor() {
        this.component = ArrayList()
    }


}