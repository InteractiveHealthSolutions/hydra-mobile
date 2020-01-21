package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab

data class LabTestAttribute(
    val attributeType: AttributeType,
    val auditInfo: AuditInfo,
    val display: String,
    val labTest: LabTest,
    val uuid: String,
    val valueReference: String
)