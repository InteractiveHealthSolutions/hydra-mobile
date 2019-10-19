package ihsinformatics.com.hydra_mobile.data.remote.model.workflow

import ihsinformatics.com.hydra_mobile.data.local.entities.workflow.Forms

class ComponentFormsObject(
    val name: String,
    val componentId: Int,
    val forms: List<Forms>
)