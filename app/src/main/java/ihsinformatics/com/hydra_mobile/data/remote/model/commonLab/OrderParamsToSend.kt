package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab

data class OrderParamsToSend(
    val careSetting: String,
    val concept: String,
    val encounter: String,
    val orderer: String,
    val patient: String,
    val type: String
)