package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab

data class TestSample(
    val collectionDate: String,
    val collector: Collector,
    val comments: Any,
    val display: Any,
    val expirable: Boolean,
    val expiryDate: Any,
    val labTest: LabTest,
    val processedDate: Any,
    val quantity: Any,
    val resourceVersion: String,
    val sampleIdentifier: Any,
    val specimenSite: SpecimenSite,
    val specimenType: SpecimenType,
    val status: String,
    val units: Any,
    val uuid: String
)