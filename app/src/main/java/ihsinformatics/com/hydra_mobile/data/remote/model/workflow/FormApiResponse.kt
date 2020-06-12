package ihsinformatics.com.hydra_mobile.data.remote.model.workflow


import com.google.gson.annotations.SerializedName
import com.ihsinformatics.dynamicformsgenerator.data.core.Form
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Component
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Forms
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Phases
import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.WorkFlow

class FormApiResponse {

    @SerializedName("forms")
    val forms: List<Forms>

    constructor(forms: List<Forms>) {
        this.forms = forms
    }

    constructor() {
        this.forms = ArrayList()
    }


}