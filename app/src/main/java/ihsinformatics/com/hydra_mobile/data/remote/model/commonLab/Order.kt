package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab

data class Order(
    val accessionNumber: Any,
    val action: String,
    val autoExpireDate: Any,
    val careSetting: CareSetting,
    val commentToFulfiller: Any,
    val concept: Concept,
    val dateActivated: String,
    val dateStopped: Any,
    val display: String,
    val encounter: Encounter,
    val instructions: Any,
    val links: List<Link>,
    val orderNumber: String,
    val orderReason: Any,
    val orderReasonNonCoded: Any,
    val orderType: OrderType,
    val orderer: Orderer,
    val patient: Patient,
    val previousOrder: Any,
    val resourceVersion: String,
    val scheduledDate: Any,
    val type: String,
    val urgency: String,
    val uuid: String
)