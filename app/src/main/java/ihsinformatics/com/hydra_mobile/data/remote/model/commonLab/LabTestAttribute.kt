package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab

data class LabTestAttribute(
    val attributeType: AttributeType,
    val auditInfo: AuditInfo,
    val display: String,
    val labTest: LabTestType,
    val uuid: String,
    val valueReference: String
)

