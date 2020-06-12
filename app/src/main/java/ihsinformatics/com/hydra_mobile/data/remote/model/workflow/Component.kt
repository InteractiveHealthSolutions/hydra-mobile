package ihsinformatics.com.hydra_mobile.data.remote.model.workflow

import ihsinformatics.com.hydra_mobile.data.remote.model.workflow.Form


class Component(
    val name: String,
    val componentId: Int,
    val forms: List<Form>
)