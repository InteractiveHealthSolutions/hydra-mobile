package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab

data class LabTestType(
    val description: String,
    val display: String,
    val name: String,
    val requiresSpecimen: Boolean,
    val shortName: String,
    val testGroup: String,
    val uuid: String,
    val referenceConcept: ReferenceConcept
)