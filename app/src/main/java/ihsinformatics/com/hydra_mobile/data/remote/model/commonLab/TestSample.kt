package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab

data class TestSample(
    val display: String,
    val specimenSite: SpecimenSite,
    val specimenType: SpecimenType,
    val status: String,
    val uuid: String
)