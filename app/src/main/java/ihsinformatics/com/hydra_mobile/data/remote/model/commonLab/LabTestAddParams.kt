package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab

data class LabTestAddParams(
    val labInstructions: String,
    val labReferenceNumber: String,
    val labTestType: String,
    val order: OrderParamsToSend
)