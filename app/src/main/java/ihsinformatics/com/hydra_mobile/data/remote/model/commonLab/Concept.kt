package ihsinformatics.com.hydra_mobile.data.remote.model.commonLab

data class Concept(
    val display: String,
    val links: List<Link>,
    val retired: Boolean,
    val uuid: String
)