package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab

data class LabTestOrder(
    val attributes: List<Any>,
    val auditInfo: AuditInfo,
    val display: String,
    val labReferenceNumber: String,
    val labTestSamples: List<TestSample>,
    val labTestType: LabTestType,
    val links: List<Link>,
    val testOrderId:String,
    val order: Order,
    val resourceVersion: String,
    val uuid: String
)