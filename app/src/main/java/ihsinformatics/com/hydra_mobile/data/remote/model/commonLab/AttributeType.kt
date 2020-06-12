package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab

data class AttributeType(
    val datatypeClassname: String,
    val datatypeConfig: String,
    val description: String,
    val display: String,
    val handlerConfig: String,
    val labTestType: LabTestType,
    val maxOccurs: Int,
    val name: String,
    val preferredHandlerClassname: String,
    val resourceVersion: String,
    val sortWeight: Double,
    val uuid: String
)